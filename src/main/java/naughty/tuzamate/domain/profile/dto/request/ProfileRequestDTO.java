package naughty.tuzamate.domain.profile.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProfileRequestDTO(
    @NotNull(message = "나이를 입력해주세요")
    Long age,
    @NotNull(message = "투자 경험을 입력해주세요")
    boolean experience,
    @NotNull(message = "닉네임을 입력해주세요")
    String nickname,
    @NotNull(message = "자금 상황을 입력해주세요")
    String funding_situation,
    @NotNull(message = "수입을 입력해주세요")
    Long income,
    @NotNull(message = "수입 안정성을 입력해주세요")
    String stability,
    @NotNull(message = "수입원을 입력해주세요")
    String source,
    @NotNull(message = "투자 경험을 입력해주세요")
    String type,
    @NotNull(message = "투자 비율을 입력해주세요")
    String proportion,
    @NotNull(message = "예상 기간을 입력해주세요")
    Long period,
    @NotNull(message = "예상 수입을 입력해주세요")
    Long expected_income,
    @NotNull(message = "예상 지출을 입력해주세요")
    Long expected_loss,
    @NotNull(message = "목적을 입력해주세요")
    String purpose
) {
}
