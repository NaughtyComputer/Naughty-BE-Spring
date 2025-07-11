package naughty.tuzamate.domain.profile.converter;

import naughty.tuzamate.domain.community.CommunityItem;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.postScrap.entity.PostScrap;
import naughty.tuzamate.domain.profile.dto.request.ProfileRequestDTO;
import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;

public class ProfileConverter {

    public static ProfileResponseDTO.updateProfileResponse toProfileResponseDTO(User updateUser) {

        return ProfileResponseDTO.updateProfileResponse.builder()
                .nickname(updateUser.getNickname())
                .build();
    }

    public static ProfileResponseDTO.getProfileResponse from(User user) {

        return ProfileResponseDTO.getProfileResponse.builder()
                .age(user.getAge())
                .experience(user.isExperience())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .funding_situation(user.getFundingSituation())
                .income(user.getIncome())
                .stability(user.getStability())
                .source(user.getSource())
                .type(user.getType())
                .proportion(user.getProportion())
                .period(user.getPeriod())
                .expected_income(user.getExpectedIncome())
                .expected_loss(user.getExpectedLoss())
                .purpose(user.getPurpose())
                .build();
    }

    public static ProfileResponseDTO.profileCommunityResponse toProfileCommunityDTO(Post post) {

        return ProfileResponseDTO.profileCommunityResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .contentPreview(post.getContent().length() > 20 ? post.getContent().substring(0, 20) + "..." : post.getPost().getContent())
                .build();
    }

    public static <T extends CommunityItem> ProfileResponseDTO.profileCommunityListResponse toProfileCommunityListDTO(Slice<T> slice) {
        return ProfileResponseDTO.profileCommunityListResponse.builder()
                .scraps(slice.getContent().isEmpty() ? new ArrayList<>() :
                        slice.getContent().stream()
                                .map(item -> ProfileConverter.toProfileCommunityDTO(item.getPost()))
                                .toList())
                .hasNextPage(slice.hasNext())
                .cursor(slice.getContent().isEmpty() ? 0L : slice.getContent().get(slice.getContent().size() - 1).getCursorId())
                .build();
    }

}
