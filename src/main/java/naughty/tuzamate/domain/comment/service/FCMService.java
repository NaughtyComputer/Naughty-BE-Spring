package naughty.tuzamate.domain.comment.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendNotification(String title, String body, String fcmToken) {
        if (title == null || body == null || fcmToken == null || fcmToken.trim().isEmpty()) {
        log.warn("Invalid notification parameters - title: {}, body: {}, fcmToken: {}", title, body, fcmToken);
        return;
    }
        log.info("Attempting to send Notification (title: {}, body: {}, fcmToken: {})", title, body, fcmToken);
        send(createMessage(title, body, fcmToken));
    }

    private void send(Message message) {
        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully send Notification: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send Notification : {}", e.getMessage());
        }
    }

    private Message createMessage(String title, String body, String fcmToken) {
        return Message.builder()
                .putData("title", title)
                .putData("body", body)
                .setToken(fcmToken)
                .build();
    }
}
