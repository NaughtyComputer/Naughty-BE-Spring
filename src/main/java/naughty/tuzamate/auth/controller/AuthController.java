package naughty.tuzamate.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.dto.TokenResponse;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.auth.service.AuthService;
import naughty.tuzamate.auth.success.AuthSuccessCode;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @GetMapping("/auth/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.")
    public CustomResponse<?> logout(HttpServletRequest request) {

        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(bearer);
        return CustomResponse.onSuccess(AuthSuccessCode.LOGOUT_SUCCESS_CODE);
    }

    @PostMapping("/auth/refresh")
    @Operation(summary = "토큰 재발급", description = "만료된 액세스 토큰을 재발급받습니다.")
    public CustomResponse<?> reissueToken(
            @Parameter(
                    name = "refreshToken",
                    description = "리프레시 토큰을 쿠키에 넣어 요청",
                    in = ParameterIn.COOKIE,
                    required = true
            )
            @CookieValue("refreshToken") String refreshToken, HttpServletResponse response
    ) {
        TokenResponse.TokenDto tokenDto = authService.reissueToken(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true) // 테스트 시 false
                .path("/")
                .sameSite("Lax")
                .maxAge(jwtProvider.getRefreshExpiration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return CustomResponse.onSuccess(AuthSuccessCode.ACCESS_TOKEN_REISSUE_SUCCESS_CODE, tokenDto);
    }
}
