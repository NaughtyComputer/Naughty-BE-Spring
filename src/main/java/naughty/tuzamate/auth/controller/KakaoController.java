package naughty.tuzamate.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.service.OAuth2Service;
import naughty.tuzamate.auth.success.AuthSuccessCode;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "카카오 로그인")
public class KakaoController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/oauth2/kakao-login")
    @Operation(summary = "카카오 로그인 백엔드 테스트용", description = "카카오 소셜 로그인 인가 코드 발급을 수행")
    public CustomResponse<?> login() {

        String code = oAuth2Service.getCode();
        return CustomResponse.onSuccess(AuthSuccessCode.AUTH_SUCCESS_CODE, code);
    }


    @GetMapping("/oauth2/tuzamate/kakao")
    // code는 카카오에서 주는 것 (디버깅으로 확인 가능)
    public CustomResponse<?> KakaoLogin(@RequestParam("code") String code) {

        UserResponseDTO.UserTokenDTO kakaoToken = oAuth2Service.login("kakao", code);
        return CustomResponse.onSuccess(kakaoToken);
    }
}
