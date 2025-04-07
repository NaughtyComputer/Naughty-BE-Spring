package naughty.tuzamate.domain.post.service.command;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.post.converter.PostConverter;
import naughty.tuzamate.domain.post.dto.PostReqDTO;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.repository.PostRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;

    @Override
    public PostResDTO.CreatePostResponseDTO createPost(PostReqDTO.CreatePostRequestDTO reqDTO) {
        // reqDTO -> Post Entity 로 변환, Post Entity -> resDTO 로 return
        Post post = PostConverter.toPost(reqDTO);
        postRepository.save(post);

        return PostConverter.toCreatePostResponseDTO(post);
    }

    @Override
    public PostResDTO.UpdatePostResponseDTO updatePost(PostReqDTO.UpdatePostRequestDTO reqDTO, Long postId) {
        // reqDTO -> Post Entity, Post Entity -> resDTO
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        if (reqDTO.title() != null) {
            post.updateTitle(reqDTO.title());
        }
        if (reqDTO.content() != null) {
            post.updateContent(reqDTO.content());
        }

        return PostConverter.toUpdatePostResponseDTO(post);
    }

    @Override
    public PostResDTO.DeletePostResponseDTO deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        postRepository.delete(post);

        return PostConverter.toDeletePostResponseDTO(postId);
    }

}
