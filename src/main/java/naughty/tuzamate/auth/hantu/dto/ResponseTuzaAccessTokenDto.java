package naughty.tuzamate.auth.hantu.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseTuzaAccessTokenDto {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String accessTokenTokenExpired;

}
