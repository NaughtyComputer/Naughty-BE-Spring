package naughty.tuzamate.domain.profile.service.query;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.profile.repository.ProfileRepository;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileQueryService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public User getProfile(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));
    }
}
