package naughty.tuzamate.domain.profile.service.command;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.profile.dto.request.DeleteUserRequestDto;
import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.profile.error.ProfileErrorCode;
import naughty.tuzamate.domain.profile.repository.ProfileRepository;
import naughty.tuzamate.domain.user.dto.UserInitProfileRequestDTO;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileCommandService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileResponseDTO.profileInitResponse initProfile(User user, UserInitProfileRequestDTO dto) {

        if (user.getNickname() != null || user.getExperience() != null) {
            throw new UserCustomException(UserErrorCode.ALREADY_INIT_PROFILE);
        }

        if (userRepository.existsByNickname(dto.nickname())) {
            throw new UserCustomException(UserErrorCode.USER_NICKNAME_ALREADY_EXISTS);
        }

        user.initProfile(dto.nickname(), dto.experience());
        userRepository.save(user);

        return ProfileResponseDTO.profileInitResponse.of(user);
    }

    public void deleteProfile(User user, DeleteUserRequestDto dto) {

        if (!user.getNickname().equals(dto.nickname())) {
            throw new CustomException(ProfileErrorCode.MISMATCHED_NICKNAME);
        }

        user.withdraw();
        userRepository.save(user);
    }
}
