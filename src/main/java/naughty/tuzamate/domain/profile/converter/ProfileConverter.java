package naughty.tuzamate.domain.profile.converter;

import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.user.entity.User;

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
}
