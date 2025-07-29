package naughty.tuzamate.domain.user.service;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.auth.service.RefreshTokenService;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.dto.UserRequestDTO;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public UserResponseDTO.UserTokenDTO login(UserRequestDTO.UserLoginDTO loginDTO) {

        User user = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(
                () -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));

        if (!encoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UserCustomException(UserErrorCode.USER_PASSWORD_INCORRECT);
        }

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        long refreshExpiration = jwtProvider.getRefreshExpiration();

        Instant issuedAt = Instant.now();
        Instant refreshExpire = issuedAt.plusMillis(refreshExpiration);

        return UserResponseDTO.UserTokenDTO.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpire(Date.from(refreshExpire))
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

    @Override
    public void updateFcmToken(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        user.updateFcmToken(token);
    }
}
