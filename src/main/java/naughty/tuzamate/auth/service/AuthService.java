package naughty.tuzamate.auth.service;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.dto.TokenResponse;
import naughty.tuzamate.auth.entity.RefreshToken;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.auth.jwt.error.JwtErrorCode;
import naughty.tuzamate.auth.repository.RefreshTokenRepository;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final StringRedisTemplate redisTemplate;
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

        redisTemplate.delete("refreshToken:" + userId);
    }

    public TokenResponse.TokenDto reissueToken(String token) {

        jwtProvider.isValid(token); // 토큰 유효성 검사

        Long userId = jwtProvider.getUserId(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));

        if (user.getTokenVersion() != jwtProvider.getTokenVersion(token)) {
            throw new CustomException(UserErrorCode.LOGGED_OUT_USER); // 의도적으로 토큰을 더 이상 신뢰하지 않는지 확인
        }

        String storedRefreshToken = redisTemplate.opsForValue().get("refreshToken:" + userId);

        if (storedRefreshToken == null || !storedRefreshToken.equals(token)) {
            throw new CustomException(UserErrorCode.LOGGED_OUT_USER); // 로그아웃된 사용자이거나 토큰이 일치하지 않음
        }

        String newAccessToken = jwtProvider.createAccessToken(user);
        String newRefreshToken = jwtProvider.createRefreshToken(user);

        redisTemplate.opsForValue().set(
                "refreshToken:" + userId,
                newRefreshToken,
                jwtProvider.getRefreshExpiration(),
                TimeUnit.MILLISECONDS
        );

        return new TokenResponse.TokenDto(newAccessToken, newRefreshToken);

    }
}
