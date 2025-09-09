package naughty.tuzamate.domain.pushToken.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import naughty.tuzamate.domain.pushToken.enums.Platform;

@Data
public class PushTokenReqDTO {

    @Builder
    public record RegisterPushTokenReqDTO(
            Long userId,

            @NotBlank
            String token,

            @NotBlank
            Platform platform,

            @NotBlank
            String deviceId
    ) {}

    @Builder
    public record UnbindPushTokenReqDTO(
            @NotBlank
            String token,

            // true -> 계정 탈퇴 및 알림 기능 해제 / false -> 로그아웃 같은 경우
            boolean deactivate
    ) {}
}
