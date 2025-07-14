package naughty.tuzamate.domain.profile.service.command;

import naughty.tuzamate.domain.user.dto.UserInitProfileRequestDTO;
import naughty.tuzamate.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileCommandServiceTest {

    @Test
    void initProfile() {

        // given
        User user = User.builder().id(1L).build();

        // when
        UserInitProfileRequestDTO dto = new UserInitProfileRequestDTO("nickname1", "beginner");
        user.initProfile(dto.nickname(), dto.experience());

        // then
        Assertions.assertThat(user.getNickname()).isEqualTo("nickname1");
        Assertions.assertThat(user.getExperience()).isEqualTo("beginner");
    }
}