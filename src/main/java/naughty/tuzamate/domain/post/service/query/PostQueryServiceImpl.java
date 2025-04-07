package naughty.tuzamate.domain.post.service.query;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.post.converter.PostConverter;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.repository.PostRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;


    // Post error 코드 및 exception만들기!!!!!!
    @Override
    public PostResDTO.PostPreviewDTO getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        return PostConverter.toPostPreviewDTO(post);
    }

    @Override
    public PostResDTO.PostPreviewListDTO getPostList() {
        return PostConverter.toPostPreviewListDTO(postRepository.findAll());
    }
}
