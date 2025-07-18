package naughty.tuzamate.domain.profile.service.query;

import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.postScrap.entity.PostScrap;
import naughty.tuzamate.domain.postScrap.repository.PostScrapRepository;
import naughty.tuzamate.domain.profile.converter.ProfileConverter;
import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileQueryServiceTest {

    @Mock
    private PostScrapRepository postScrapRepository;

    @InjectMocks
    private ProfileQueryService profileQueryService;

    @Test
    @DisplayName("cursor != 0 일 때 scrapList 조회")
    void scrapList_cursor_positive() {

        // given
        // 유저 생성
        User user = User.builder().id(1L).build();

        // 옵셋 및 cursor 설정
        int offset = 10;
        Long cursor = 5L;


        SliceImpl<PostScrap> postScraps = new SliceImpl<>(
                List.of(
                        PostScrap.builder().id(1L).post(Post.builder().id(1L).title("title1").content("content1").build()).build(),
                        PostScrap.builder().id(2L).post(Post.builder().id(2L).title("title2").content("content2").build()).build()
                ),
                PageRequest.of(0, offset),
                false
        );

        when(postScrapRepository.findScrapListByUserIdLessThanOrderByIdDesc(eq(user.getId()), eq(cursor), any()))
                .thenReturn(postScraps);

        ProfileResponseDTO.profileCommunityListResponse expectedResponse = new ProfileResponseDTO.profileCommunityListResponse(List.of(), false, 5L);

        try (MockedStatic<ProfileConverter> mocked =
                     mockStatic(ProfileConverter.class)) {
            mocked.when(() -> ProfileConverter.toProfileCommunityListDTO(postScraps))
                    .thenReturn(expectedResponse);

            // when
            ProfileResponseDTO.profileCommunityListResponse actualResponse = profileQueryService.getScrapList(user.getId(), cursor, offset);

            // then
            Assertions.assertThat(actualResponse).isSameAs(expectedResponse);
        }

    }

    @Test
    @DisplayName("cursor != 0일 때 scrapList 조회 - 실제 동작")
    void scrapList_cursor_positive_real() {

        // given
        // 유저 생성
        User user = User.builder().id(1L).build();

        // 옵셋 및 cursor 설정
        int offset = 10;
        Long cursor = 5L;

        SliceImpl<PostScrap> postScraps = new SliceImpl<>(
                List.of(
                        PostScrap.builder().id(1L).post(Post.builder().id(1L).title("title1").content("content1").build()).build(),
                        PostScrap.builder().id(2L).post(Post.builder().id(2L).title("title2").content("content2").build()).build()
                ),
                PageRequest.of(0, offset),
                false
        );

        when(postScrapRepository.findScrapListByUserIdLessThanOrderByIdDesc(eq(user.getId()), eq(cursor), any()))
                .thenReturn(postScraps);

        // when
        ProfileResponseDTO.profileCommunityListResponse actualResponse = profileQueryService.getScrapList(user.getId(), cursor, offset);

        // then
        Assertions.assertThat(actualResponse.scraps())
                .extracting("postId", "title", "contentPreview")
                .containsExactly(
                        Tuple.tuple(1L, "title1", "content1"),
                        Tuple.tuple(2L, "title2", "content2")
                );

        Assertions.assertThat(actualResponse.hasNextPage()).isFalse();
        Assertions.assertThat(actualResponse.cursor()).isEqualTo(2L); // 마지막 PostScrap의 id가 2이므로 cursor는 2L이어야 함
    }
}