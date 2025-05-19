package naughty.tuzamate.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.service.OAuth2Service;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "카카오 로그인")
public class KakaoController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/oauth2/tuzamate/kakao")
    // code는 카카오에서 주는 것 (디버깅으로 확인 가능)
    public CustomResponse<?> KakaoLogin(@RequestParam("code") String code) {
        System.out.println(code);
        return CustomResponse.onSuccess(oAuth2Service.login("kakao", code));
    }
}
