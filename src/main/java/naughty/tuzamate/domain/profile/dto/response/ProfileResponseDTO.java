package naughty.tuzamate.domain.profile.dto.response;

import lombok.Builder;

public class ProfileResponseDTO {

    @Builder
    public record updateProfileResponse(
            String nickname
    ) {}

    @Builder
    public record getProfileResponse(
            Long age,
            boolean experience,
            String email,
            String nickname,
            String funding_situation,
            Long income,
            String stability,
            String source,
            String type,
            String proportion,
            Long period,
            Long expected_income,
            Long expected_loss,
            String purpose
    ) {}


}
