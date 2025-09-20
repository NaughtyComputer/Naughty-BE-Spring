package naughty.tuzamate.domain.pushToken.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.pushToken.FcmSender;
import naughty.tuzamate.domain.pushToken.dto.PushTokenSendDTO;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// test 용
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/push")
// @PreAuthorize("hasRole('ADMIN')") // 운영/관리자 전용 권장
public class PushMessageController {

    private final FcmSender fcmSender;

    @PostMapping("/token")
    public CustomResponse<String> sendToToken(@Valid @RequestBody PushTokenSendDTO.SendToTokenRequest req) throws Exception {
        String messageId = fcmSender.sendToToken(
                req.token(),
                req.title(),
                req.body(),
                req.data(),
                Boolean.TRUE.equals(req.highPriority()),
                req.clickAction()
        );
        return CustomResponse.onSuccess(messageId);
    }

    @PostMapping("/tokens")
    public CustomResponse<FcmSender.BatchResult> sendToTokens(@Valid @RequestBody PushTokenSendDTO.SendToTokensRequest req) throws Exception {
        FcmSender.BatchResult result = fcmSender.sendToTokens(
                req.tokens(),
                req.title(),
                req.body(),
                req.data(),
                Boolean.TRUE.equals(req.highPriority()),
                req.clickAction()
        );
        return CustomResponse.onSuccess(result);
    }
}

