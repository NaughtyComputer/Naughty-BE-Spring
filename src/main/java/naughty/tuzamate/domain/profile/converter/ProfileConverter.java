package naughty.tuzamate.domain.profile.converter;

import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.user.entity.User;

public class ProfileConverter {

    public static ProfileResponseDTO toProfileResponseDTO(User updateUser) {

        return new ProfileResponseDTO(
                updateUser.getNickname()
        );
    }
}
