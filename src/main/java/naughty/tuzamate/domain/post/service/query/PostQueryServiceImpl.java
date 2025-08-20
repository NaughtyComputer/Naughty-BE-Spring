package naughty.tuzamate.domain.post.service.query;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.post.code.PostErrorCode;
import naughty.tuzamate.domain.post.converter.PostConverter;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.enums.BoardType;
import naughty.tuzamate.domain.post.repository.PostRepository;
import naughty.tuzamate.domain.postLike.repository.PostLikeRepository;
import naughty.tuzamate.domain.postScrap.repository.PostScrapRepository;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;

    @Override
    @Transactional
    public PostResDTO.PostDTO getPost(Long postId, PrincipalDetails principalDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        if (!post.isRead()) {
            post.setIsRead();
        }

        boolean liked = postLikeRepository.existsByPostIdAndUserId(postId, principalDetails.getId());
        boolean scraped = postScrapRepository.existsByPostIdAndUserId(postId, principalDetails.getId());

        return PostConverter.toPostDTO(post, liked, scraped);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResDTO.PostPreviewListDTO getPostList(BoardType boardType, Long cursor, int size) {
        cursor = normalizeCursor(cursor);
        size = normalizeSize(size);

        Pageable pageable = PageRequest.of(0, size);
        Slice<Post> slice = postRepository.findByBoardTypeAndCursor(boardType, cursor, pageable);

        List<PostResDTO.PostPreviewDTO> previews = slice.stream()
                .map(PostConverter::toPostPreviewDTO)
                .toList();

        Long nextCursor = (slice.hasNext() && !previews.isEmpty()) ? previews.get(previews.size() - 1).id() : null;

        return PostResDTO.PostPreviewListDTO.builder()
                .postPreviewDTOList(previews)
                .nextCursor(nextCursor)
                .hasNext(slice.hasNext())
                .build();
    }

    private Long normalizeCursor(Long cursor) {
        return (cursor == null || cursor == 0) ? Long.MAX_VALUE : cursor;
    }

    // 요청 사이즈가 1보다 작으면 기본값 10, 10보다 크면 최대값 10으로 제한
    private int normalizeSize(int size) {
        return (size < 1 || size > 10) ? 10 : size;
    }
}