package naughty.tuzamate.domain.profile.service.command;

import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.user.dto.UserInitProfileRequestDTO;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static reactor.core.publisher.Mono.when;

class ProfileCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileCommandService profileCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void initProfile() {

        // given
        User user = User.builder().id(1L).build();
        UserInitProfileRequestDTO dto = new UserInitProfileRequestDTO("nickname1", "beginner");

        // when
        ProfileResponseDTO.profileInitResponse response = profileCommandService.initProfile(user, dto);

        // then
        Assertions.assertThat(response.nickname()).isEqualTo(user.getNickname());
        Assertions.assertThat(user.getExperience()).isEqualTo(user.getExperience());
    }
}