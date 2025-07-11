package naughty.tuzamate.domain.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Slice;

import java.util.List;

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

    @Builder
    public record profileCommunityResponse(
            Long postId,
            String title,
            String contentPreview
    ) {}

    @Builder
    public record profileCommunityListResponse(
            List<profileCommunityResponse> scraps,
            boolean hasNextPage,
            Long cursor
    ) {}



}
