package naughty.tuzamate.domain.profile.service.query;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.postScrap.entity.PostScrap;
import naughty.tuzamate.domain.postScrap.repository.PostScrapRepository;
import naughty.tuzamate.domain.profile.converter.ProfileConverter;
import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileQueryService {

    private final UserRepository userRepository;
    private final PostScrapRepository postScrapRepository;

    public User getProfile(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));
    }

    public ProfileResponseDTO.scrapListResponse getScrapList(User user, Long cursor, int offset) {

        Pageable pageable = PageRequest.of(0, offset);

        Slice<PostScrap> postScraps = null;

        if (cursor == 0) {
            postScraps = postScrapRepository.findScrapListByUserIdOrderByIdDesc(user.getId(), pageable);
        } else {
            postScraps = postScrapRepository.findScrapListByUserIdLessThanOrderByIdDesc(user.getId(), cursor, pageable);
        }

        return ProfileConverter.toScrapListResponse(postScraps);
    }
}
