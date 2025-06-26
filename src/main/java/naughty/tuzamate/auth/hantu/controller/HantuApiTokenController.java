package naughty.tuzamate.auth.hantu.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.hantu.service.HantuApiTokenService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HantuApiTokenController {

    private final HantuApiTokenService hantuApiTokenService;

    @PostMapping("/hantu/accessToken")
    @Tag(name = "한투 액세스 키 발급", description = "수동으로 한국 투자 증권 액세스 키 발급")
    public CustomResponse<?> getHantuzaAccessToken() {

        boolean result = hantuApiTokenService.refreshAccessToken();
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, "한국투자증권 액세스 키 발급 상태: "+result);
    }
}
