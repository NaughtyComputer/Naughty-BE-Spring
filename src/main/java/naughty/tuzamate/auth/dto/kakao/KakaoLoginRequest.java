package naughty.tuzamate.auth.dto.kakao;

import jakarta.validation.constraints.NotNull;

public record KakaoLoginRequest(
    @NotNull String accessToken
) {
}
