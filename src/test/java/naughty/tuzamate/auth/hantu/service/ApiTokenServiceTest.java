package naughty.tuzamate.auth.hantu.service;

import naughty.tuzamate.auth.hantu.HantuApiTokenInMemoryStore;
import naughty.tuzamate.auth.hantu.dto.ResponseHantuAccessTokenDto;
import naughty.tuzamate.auth.hantu.repository.HantuApiTokenStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/*@TestPropertySource(properties = {
        "APP_KEY=test-app-key",
        "APP_SECRET_KEY=test-secret-key",
        "URL=test_url"
}) --> @ExtendWith 사용하면 적용이 되지 않는다. 그래서 ApiTokenService에서 생성자 주입을 적용하였다. */
@ExtendWith(MockitoExtension.class)
class ApiTokenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private HantuApiTokenService apiTokenService;
    private HantuApiTokenStore apiTokenStore;

    @BeforeEach
    void before() {
        apiTokenStore = new HantuApiTokenInMemoryStore();
        apiTokenService = new HantuApiTokenService(apiTokenStore, restTemplate, "test-app-key", "test-secret-key", "test-url");
    }

    @Test
    void 토큰X() {

        String currentAccessToken = apiTokenService.getCurrentAccessToken();
        assertThat(currentAccessToken).isNull();
    }

    @Test
    void 액세스토큰갱신성공() {

        // given
        ResponseHantuAccessTokenDto accessToeknResponse = ResponseHantuAccessTokenDto.builder().accessToken("new-access-token").build();

        // when
        when(restTemplate.postForEntity(anyString(), any(), eq(ResponseHantuAccessTokenDto.class)))
                .thenReturn((ResponseEntity<ResponseHantuAccessTokenDto>) new ResponseEntity<>(accessToeknResponse, HttpStatus.OK));

        boolean result = apiTokenService.refreshAccessToken();

        // then
        assertThat(result).isTrue();
        assertThat(apiTokenService.getCurrentAccessToken()).isEqualTo("new-access-token");

    }
}