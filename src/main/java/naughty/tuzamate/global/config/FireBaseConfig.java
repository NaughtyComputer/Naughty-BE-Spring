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
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FireBaseConfig {

    @Value("${firebase.service-account.path}")
    private String serviceAccountPath;

    @Bean
    public FirebaseApp firebaseApp() {
        try (InputStream serviceAccount = getServiceAccountStream()) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            log.info(" Successfully initialized firebase app");
            return FirebaseApp.initializeApp(options);

        } catch (IOException exception) {
            log.error(" Fail to initialize firebase app: {}", exception.getMessage(), exception);
            return null;
        }
    }

    private InputStream getServiceAccountStream() throws IOException {
        // 절대경로면 FileInputStream, 아니면 classpath
        if (serviceAccountPath.startsWith("/") || serviceAccountPath.contains(":")) {
            log.info("Using absolute path for Firebase service account: {}", serviceAccountPath);
            return new FileInputStream(serviceAccountPath);
        } else {
            log.info("Using classpath resource for Firebase service account: {}", serviceAccountPath);
            return new ClassPathResource(serviceAccountPath).getInputStream();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
