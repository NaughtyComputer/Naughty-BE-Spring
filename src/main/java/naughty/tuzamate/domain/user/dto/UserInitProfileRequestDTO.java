package naughty.tuzamate.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record UserInitProfileRequestDTO(

        @NotNull(message = "닉네임을 입력해주세요")
        String nickname,
        @NotNull(message = "재테크 수준을 입력해주세요")
        String experience
){}
