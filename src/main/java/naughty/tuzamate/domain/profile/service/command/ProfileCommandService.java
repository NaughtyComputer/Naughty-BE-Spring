package naughty.tuzamate.domain.profile.service.command;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.profile.dto.request.ProfileRequestDTO;
import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.profile.repository.ProfileRepository;
import naughty.tuzamate.domain.user.dto.UserInitProfileRequestDTO;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileCommandService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public User updateProfile(Long userId, ProfileRequestDTO requestDTO) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));
        user.updateNickname(requestDTO.nickname());

        return user;
    }

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
}
