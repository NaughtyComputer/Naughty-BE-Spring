package naughty.tuzamate.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FireBaseConfig {

    // String 대신 Resource 타입으로 주입받음
    @Value("${firebase.service-account.path}")
    private Resource serviceAccountResource;

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            // 주입받은 Resource에서 바로 InputStream을 얻음
            InputStream serviceAccount = serviceAccountResource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // 앱이 이미 초기화되었는지 확인 (중복 초기화 방지)
            if (FirebaseApp.getApps().isEmpty()) {
                log.info("Successfully initialized firebase app");
                return FirebaseApp.initializeApp(options);
            } else {
                return FirebaseApp.getInstance();
            }

        } catch (IOException exception) {
            log.error("Fail to initialize firebase app: {}", exception.getMessage(), exception);
            // 초기화 실패 시 null 대신 예외를 던져서 애플리케이션이 문제를 인지하게 하는 것이 더 좋습니다.
            throw new RuntimeException("Failed to initialize Firebase app.", exception);
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
