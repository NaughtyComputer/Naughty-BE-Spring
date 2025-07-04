package naughty.tuzamate.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.service.AuthService;
import naughty.tuzamate.auth.success.AuthSuccessCode;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.")
    public CustomResponse<?> logout(HttpServletRequest request) {

        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(bearer);
        return CustomResponse.onSuccess(AuthSuccessCode.LOGOUT_SUCCESS_CODE);
    }
}
