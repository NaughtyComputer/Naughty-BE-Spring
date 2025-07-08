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

    public TokenResponse.TokenDto reissueToken(String token) {

        jwtProvider.isValid(token); // 토큰 유효성 검사

        Long userId = jwtProvider.getUserId(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));

        if (user.getTokenVersion() != jwtProvider.getTokenVersion(token)) {
            throw new CustomException(UserErrorCode.LOGGED_OUT_USER); // 의도적으로 토큰을 더 이상 신뢰하지 않는지 확인
        }

        RefreshToken storedToken = refreshTokenRepository.findById(userId).orElseThrow(
                () -> new CustomException(JwtErrorCode.TOKEN_INVALID));

        // 기존 리플레쉬 토큰을 재사용하지 못하도록 한다. (if문의 다음 코드를 사용하지 못하게)
        if (!storedToken.getToken().equals(token)) { // 폐기된 토큰을 재사용 -> 탈취된 것
            refreshTokenRepository.deleteById(userId);
            throw new CustomException(JwtErrorCode.TOKEN_INVALID); // 저장된 토큰과 일치하지 않으면 예외 발생
        }

        String newAccessToken = jwtProvider.createAccessToken(user);
        String newRefreshToken = jwtProvider.createRefreshToken(user);

        storedToken.update(newRefreshToken);

        return new TokenResponse.TokenDto(newAccessToken, newRefreshToken);

    }
}
