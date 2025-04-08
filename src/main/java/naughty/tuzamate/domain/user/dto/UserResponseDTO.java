package naughty.tuzamate.domain.user.dto;

import lombok.*;

public class UserResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserTokenDTO {

        private String accessToken;
        private String refreshToken;

    }

}
