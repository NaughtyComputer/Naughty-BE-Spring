package naughty.tuzamate.auth.dto;

import lombok.Getter;

public class TokenResponse {

    @Getter
    public static class TokenDto {

        private final String accessToken;
        private final String refreshToken;

        public TokenDto(String newAccessToken, String newRefreshToken) {
            this.accessToken = newAccessToken;
            this.refreshToken = newRefreshToken;
        }
    }

}
