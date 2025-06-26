package naughty.tuzamate.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.constant.OAUTH_URL;
import naughty.tuzamate.auth.dto.kakao.KakaoOAuth2DTO;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.domain.user.enums.SocialType;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenURI;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoURI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectURI;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";


    @Override
    public String getCode() {
        return getAuthUrl();
    }

    private String getAuthUrl() {
        return OAUTH_URL.KAKAO_AUTH_URL.getUrl()
                + "?response_type=" + RESPONSE_TYPE
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectURI;

    }

    @Override
    public UserResponseDTO.UserTokenDTO login(String provider, String code) {

        if (provider.equalsIgnoreCase(SocialType.KAKAO.name())) {
            return loginWithKakao(code);
        } else {
            throw new UserCustomException(UserErrorCode.UNSUPPORTED_OAUTH_TYPE);
        }
    }



    private UserResponseDTO.UserTokenDTO loginWithKakao(String code) {

        String token = getAccessTokenFromKakao(code);
        KakaoOAuth2DTO.KakaoProfile profile = getProfileFromKakao(token);
        String email = profile.getKakao_account().getEmail();

        return loginAndSignUp(SocialType.KAKAO, email);

    }

    private UserResponseDTO.UserTokenDTO loginAndSignUp(SocialType socialType, String email) {

        User user;
        Optional<User> userEmail = userRepository.findByEmail(email);
        user = userEmail.orElseGet(() ->
                userRepository.save(User.builder()
                        .email(email)
                        .role("ROLE_USER")
                        .socialType(socialType)
                        .build()));

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        long refreshExpiration = jwtProvider.getRefreshExpiration();

        Instant issuedAt = Instant.now();
        Instant refreshExpire = issuedAt.plusMillis(refreshExpiration);

        return UserResponseDTO.UserTokenDTO.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpire(Date.from(refreshExpire))
                .build();
    }

    private KakaoOAuth2DTO.KakaoProfile getProfileFromKakao(String accessToken) {

        // 액세스 토큰으로 사용자 정보를 가져온다
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap> request1 = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response2 = restTemplate.exchange(
                userInfoURI,
                HttpMethod.GET,
                request1,
                String.class
        );

        ObjectMapper om = new ObjectMapper();

        try {
            return om.readValue(response2.getBody(), KakaoOAuth2DTO.KakaoProfile.class);
        } catch (Exception e) {
            throw new UserCustomException(UserErrorCode.OAUTH_USER_INFO_FAIL);
        }
    }


    private String getAccessTokenFromKakao(String accessCode) {

        // accessCode => 인가코드
        // 인가코드로 토큰을 가져온다
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded"); // 헤더 설정

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("redirect_uri", redirectURI);
        map.add("code", accessCode);
        HttpEntity<MultiValueMap> request = new HttpEntity<>(map, httpHeaders);

        ResponseEntity<String> response1 = restTemplate.exchange(
                tokenURI,
                HttpMethod.POST,
                request,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOAuth2DTO.OAuth2TokenDTO oAuth2TokenDTO = null;

        try {
            oAuth2TokenDTO = objectMapper.readValue(response1.getBody(), KakaoOAuth2DTO.OAuth2TokenDTO.class);
            return oAuth2TokenDTO.getAccess_token();
        } catch (Exception e) {
            throw new UserCustomException(UserErrorCode.OAUTH_TOKEN_FAIL);
        }
    }
}



