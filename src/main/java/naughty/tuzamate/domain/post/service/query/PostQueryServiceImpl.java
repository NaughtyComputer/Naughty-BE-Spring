package naughty.tuzamate.domain.post.service.query;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.post.converter.PostConverter;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.enums.BoardType;
import naughty.tuzamate.domain.post.repository.PostRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public PostResDTO.PostPreviewDTO getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        if (!post.isRead()) {
            post.setIsRead();
        }

        return PostConverter.toPostPreviewDTO(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResDTO.PostPreviewListDTO getPostList(BoardType boardType, Long cursor, int size) {
        PageRequest pr = PageRequest.of(0, size);
        Slice<Post> slice = postRepository.findByBoardTypeAndCursor(boardType, cursor, pr);

        List<PostResDTO.PostPreviewDTO> previews = slice.getContent()
                .stream()
                .map(PostConverter::toPostPreviewDTO)
                .toList();

        Long nextCursor = slice.hasNext() ? previews.get(previews.size() - 1).id() : null;

        return PostResDTO.PostPreviewListDTO.builder()
                .postPreviewDTOList(previews)
                .nextCursor(nextCursor)
                .hasNext(slice.hasNext())
                .build();
    }
}
