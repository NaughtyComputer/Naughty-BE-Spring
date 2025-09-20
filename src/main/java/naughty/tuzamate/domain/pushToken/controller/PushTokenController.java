package naughty.tuzamate.domain.pushToken.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.pushToken.dto.PushTokenReqDTO;
import naughty.tuzamate.domain.pushToken.entity.PushToken;
import naughty.tuzamate.domain.pushToken.service.PushTokenService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/push-tokens")
public class PushTokenController {

    private final PushTokenService pushTokenService;

    @PostMapping
    public CustomResponse<PushToken> register(
            @Valid @RequestBody PushTokenReqDTO.RegisterPushTokenReqDTO request,
                @AuthenticationPrincipal PrincipalDetails principal
    ) {
        PushToken token = pushTokenService.upsert(request, principal.getId());

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, token);
    }

    // 토큰 해제(로그아웃 -> deactivate = false, 탈퇴 -> deactivate = false)
    @DeleteMapping
    public CustomResponse<?> unbind(
            @Valid @RequestBody PushTokenReqDTO.UnbindPushTokenReqDTO request
    ) {
        pushTokenService.unbind(request.token(), request.deactivate());

        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }

    @PostMapping("/heartbeat")
    public CustomResponse<?> heartbeat(
            @Valid @RequestBody PushTokenReqDTO.RegisterPushTokenReqDTO req,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        pushTokenService.touch(req.token(), principal.getId());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }
}
