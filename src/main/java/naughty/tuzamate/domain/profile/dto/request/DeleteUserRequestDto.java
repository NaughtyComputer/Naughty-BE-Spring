package naughty.tuzamate.domain.profile.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeleteUserRequestDto(
        @NotNull(message = "탈퇴하기 위해 닉네임을 입력해야 합니다.")
        String nickname
) {
}
