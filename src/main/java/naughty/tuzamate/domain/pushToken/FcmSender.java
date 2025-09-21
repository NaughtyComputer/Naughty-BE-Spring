package naughty.tuzamate.domain.pushToken;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmSender {

    // 단일 토큰 전송: 전송/로깅만, DB 변경은 하지 않음
    public String sendToToken(
            String token, String title, String body, Map<String, String> data,
            boolean highPriority, String clickAction
    ) throws Exception {

        Message.Builder mb = Message.builder().setToken(token);
        applyCommonNotification(mb, title, body);
        applyCommonData(mb, data);
        mb.setAndroidConfig(buildAndroidConfig(highPriority, clickAction));

        try {
            return FirebaseMessaging.getInstance().send(mb.build());
        } catch (FirebaseMessagingException e) {
            log.warn("FCM single send failed token={}, code={}", token, e.getMessagingErrorCode(), e);
            throw e;
        }
    }

    // 다중 토큰 전송
    public BatchResult sendToTokens(
            List<String> tokens, String title, String body, Map<String, String> data,
            boolean highPriority, String clickAction
    ) throws Exception {

        if (tokens == null || tokens.isEmpty()) {
            return new BatchResult(0, 0, List.of());
        }

        int totalSuccess = 0;
        int totalFailure = 0;

        List<String> permanentFailed = new ArrayList<>();

        for (int start = 0; start < tokens.size(); start += 500) {
            List<String> chunk = tokens.subList(start, Math.min(start + 500, tokens.size()));

            MulticastMessage.Builder mb = MulticastMessage.builder().addAllTokens(chunk);
            applyCommonNotification(mb, title, body);
            applyCommonData(mb, data);
            mb.setAndroidConfig(buildAndroidConfig(highPriority, clickAction));

            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(mb.build());

            totalSuccess += response.getSuccessCount();
            totalFailure += response.getFailureCount();

            List<SendResponse> rs = response.getResponses();

            for (int i = 0; i < rs.size(); i++) {
                SendResponse r = rs.get(i);

                if (!r.isSuccessful()) {
                    String t = chunk.get(i);
                    Exception ex = r.getException();

                    if (ex instanceof FirebaseMessagingException fme) {
                        MessagingErrorCode code = fme.getMessagingErrorCode();
                        log.warn("FCM multicast failed token={}, code={}", t, code, fme);

                        // 영구 실패만 수집 (일시 실패는 수집하지 않음)
                        if (isPermanentFailure(fme)) {
                            permanentFailed.add(t);
                        }
                    } else if (ex != null) {
                        log.warn("FCM multicast failed token={} (non-Firebase exception)", t, ex);
                    }
                }
            }
        }

        return new BatchResult(totalSuccess, totalFailure, permanentFailed);
    }

    // 영구 실패(비활성화 대상) 판정
    private boolean isPermanentFailure(FirebaseMessagingException e) {
        MessagingErrorCode c = e.getMessagingErrorCode();

        return c == MessagingErrorCode.UNREGISTERED
                || c == MessagingErrorCode.INVALID_ARGUMENT
                || c == MessagingErrorCode.SENDER_ID_MISMATCH
                || c == MessagingErrorCode.THIRD_PARTY_AUTH_ERROR;
    }

    // ===== 공통 빌더 유틸 =====
    private void applyCommonNotification(Message.Builder b, String title, String body) {
        if (title != null || body != null) {
            b.setNotification(Notification.builder().setTitle(title).setBody(body).build());
        }
    }
    private void applyCommonNotification(MulticastMessage.Builder b, String title, String body) {
        if (title != null || body != null) {
            b.setNotification(Notification.builder().setTitle(title).setBody(body).build());
        }
    }

    private void applyCommonData(Message.Builder b, Map<String, String> data) {
        if (data != null && !data.isEmpty()) b.putAllData(data);
    }
    private void applyCommonData(MulticastMessage.Builder b, Map<String, String> data) {
        if (data != null && !data.isEmpty()) b.putAllData(data);
    }

    private AndroidConfig buildAndroidConfig(boolean highPriority, String clickAction) {
        AndroidConfig.Builder ab = AndroidConfig.builder();
        if (highPriority) ab.setPriority(AndroidConfig.Priority.HIGH);
        if (clickAction != null && !clickAction.isBlank()) {
            ab.setNotification(AndroidNotification.builder().setClickAction(clickAction).build());
        }
        return ab.build();
    }

    // 결과 DTO
    public record BatchResult(int success, int failure, List<String> failedTokens) {}
}

