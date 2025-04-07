package naughty.tuzamate.domain.post.service.command;

import naughty.tuzamate.domain.post.dto.PostReqDTO;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;

public interface PostCommandService {

    PostResDTO.CreatePostResponseDTO createPost(PostReqDTO.CreatePostRequestDTO reqDTO);
    PostResDTO.UpdatePostResponseDTO updatePost(PostReqDTO.UpdatePostRequestDTO reqDTO, Long postId);
    PostResDTO.DeletePostResponseDTO deletePost(Long postId);
}
