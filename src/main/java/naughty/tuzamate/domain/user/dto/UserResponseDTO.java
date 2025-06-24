package naughty.tuzamate.domain.user.dto;

import lombok.*;
import naughty.tuzamate.domain.user.entity.User;

public class UserResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserTokenDTO {

        private String accessToken;
        private String refreshToken;

    }

}
