package naughty.tuzamate.domain.profile.converter;

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
                .funding_situation(user.getFunding_situation())
                .income(user.getIncome())
                .stability(user.getStability())
                .source(user.getSource())
                .type(user.getType())
                .proportion(user.getProportion())
                .period(user.getPeriod())
                .expected_income(user.getExpected_income())
                .expected_loss(user.getExpected_loss())
                .purpose(user.getPurpose())
                .build();
    }

    public static ProfileResponseDTO.scrapResponse toPostScrapResponseDTO(PostScrap postScrap) {

        return ProfileResponseDTO.scrapResponse.builder()
                .postId(postScrap.getId())
                .title(postScrap.getPost().getTitle())
                .contentPreview(postScrap.getPost().getContent().length() > 20 ? postScrap.getPost().getContent().substring(0, 20) + "..." : postScrap.getPost().getContent())
                .build();
    }

    public static ProfileResponseDTO.scrapListResponse toScrapListResponse(Slice<PostScrap> postScraps) {
        return ProfileResponseDTO.scrapListResponse.builder()
                .scraps(postScraps.getContent().isEmpty() ? new ArrayList<>() :
                        postScraps.getContent().stream()
                                .map(postScrap -> toPostScrapResponseDTO(postScrap))
                                .toList())
                .hasNextPage(postScraps.hasNext())
                .cursor(postScraps.getContent().isEmpty() ? 0L : postScraps.getContent().get(postScraps.getContent().size() - 1).getId())
                .build();
    }

}
