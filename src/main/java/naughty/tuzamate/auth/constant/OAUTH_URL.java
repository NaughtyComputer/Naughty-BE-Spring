package naughty.tuzamate.auth.constant;

import lombok.Getter;

/**
 * OAuth2 인증을 위한 URL 상수 클래스
 * 각 OAuth2 제공자의 인증 URL을 정의한다.
 */
@Getter
public enum OAUTH_URL {

    KAKAO_AUTH_URL("https://kauth.kakao.com/oauth/authorize"), // 인가 코드 받기
    ;

    private final String url;

    OAUTH_URL(String url) {
        this.url = url;
    }
}
