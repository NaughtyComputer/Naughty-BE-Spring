package naughty.tuzamate.auth.hantu;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naughty.tuzamate.auth.hantu.service.ApiTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 토큰 자동 갱신 스케줄러
 * 24시간마다 한 번씩
 * 잦은 AccessToken 발급은 API 사용이 제한될 수 있다고 한다.
 * 따라서 실제 서비스 전까지는 주석처리를 해놓는다.
 */

@RequiredArgsConstructor
@Component
@Slf4j
public class ApiTokenRefreshScheduler {

    /* private final ApiTokenService apiTokenService;

    @PostConstruct
    public void firstToken() {

        log.info("처음 HanTu API AccessToken 발급");
        boolean result = apiTokenService.refreshAccessToken();
        if (!result) {
            log.error("API AccessToken 발급 실패");
        }
    }

    // 초, 분, 시, 일, 월, 요일
    @Scheduled(cron = "0 10 17 * * *")
    public void refreshDailyToken() {

        boolean result = apiTokenService.refreshAccessToken();
        if (result) {
            log.info("API AccessToken 갱신 완료");
        } else {
            log.error("API AccessToken 갱신 실패");
        }
    }*/
}
