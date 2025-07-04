package naughty.tuzamate.auth.service;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.auth.jwt.error.JwtErrorCode;
import naughty.tuzamate.auth.repository.RefreshTokenRepository;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /**
     * user의 tokenVersion을 1 증가시킨다. -> 기존 토큰을 무효화 시킨다
     * DB에 저장된 refreshToken을 삭제한다.
     */
    public void logout(String bearer) {

        if (bearer == null || !bearer.startsWith("Bearer ")) {
            throw new CustomException(JwtErrorCode.JWT_BAD_REQUEST_400); //잘못된 형식의 토큰입니다.
        }

        String token = bearer.substring(7);

        jwtProvider.isValid(token);

        Long userId = jwtProvider.getUserId(token);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));
        user.increaseTokenVersion();

        refreshTokenRepository.deleteById(userId);
    }
}
