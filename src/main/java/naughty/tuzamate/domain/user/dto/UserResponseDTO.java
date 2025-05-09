package naughty.tuzamate.domain.user.dto;

import lombok.*;
import naughty.tuzamate.domain.user.entity.User;

public class UserResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserTokenDTO {

        private String accessToken;
        private String refreshToken;

    }


    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserProfileDTO {

        private Long age;
        private boolean experience;
        private String email;
        private String nickname;
        private String funding_situation;
        private Long income;
        private String stability;
        private String source;
        private String type;
        private String proportion;
        private Long period;
        private Long expected_income;
        private Long expected_loss;
        private String purpose;

        public static UserProfileDTO from(User user) {

            return UserProfileDTO.builder()
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


    }
}
