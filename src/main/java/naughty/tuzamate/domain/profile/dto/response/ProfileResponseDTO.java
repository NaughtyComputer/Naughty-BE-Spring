package naughty.tuzamate.domain.profile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import naughty.tuzamate.domain.user.dto.UserInitProfileRequestDTO;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.enums.Gender;
import org.springframework.data.domain.Slice;

import java.util.List;

public class ProfileResponseDTO {

    @Builder
    public record updateProfileResponse(
            String nickname
    ) {
    }

    @Builder
    public record getProfileResponse(
            MyInfo myInfo,
            InvestmentInfo investmentInfo
    ) {
    }

    @Builder
    public record profileCommunityResponse(
            Long postId,
            String title,
            String contentPreview
    ) {
    }

    @Builder
    public record profileCommunityListResponse(
            List<profileCommunityResponse> scraps,
            boolean hasNextPage,
            Long cursor
    ) {
    }

    public record profileInitResponse(
            String nickname,
            String experience
    ) {
        public static profileInitResponse of(User user) {
            return new profileInitResponse(user.getNickname(), user.getExperience());
        }
    }

    @Builder
    public record MyInfo(
            String nickname,
            Gender gender,
            String email
    ) {}

    @Builder
    public record InvestmentInfo(
            Long income,
            String type,
            String purpose
    ) { }


}
