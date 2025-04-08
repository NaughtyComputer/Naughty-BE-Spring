package naughty.tuzamate.domain.user.service;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.domain.user.domain.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.dto.UserRequestDTO;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserResponseDTO.UserTokenDTO login(UserRequestDTO.UserLoginDTO loginDTO) {

        User user = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(
                () -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));

        if (!encoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UserCustomException(UserErrorCode.USER_PASSWORD_INCORRECT);
        }

        return UserResponseDTO.UserTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(user))
                .refreshToken(jwtProvider.createRefreshToken(user))
                .build();
    }

    @Override
    public UserResponseDTO.UserTokenDTO signUp(UserRequestDTO.UserSignUpDTO signUpDTO) {

        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new UserCustomException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        User newUser = userRepository.save(User.builder()
                .email(signUpDTO.getEmail())
                .password(encoder.encode(signUpDTO.getPassword()))
                .role("ROLE_USER")
                .build());

        return UserResponseDTO.UserTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(newUser))
                .refreshToken(jwtProvider.createRefreshToken(newUser))
                .build();
    }
}
