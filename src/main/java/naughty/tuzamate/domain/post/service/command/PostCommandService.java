package naughty.tuzamate.domain.post.service.command;

import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.post.dto.PostReqDTO;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.enums.BoardType;

public interface PostCommandService {

    PostResDTO.CreatePostResponseDTO createPost(BoardType boardType, PostReqDTO.CreatePostRequestDTO reqDTO, PrincipalDetails principalDetails);
    PostResDTO.UpdatePostResponseDTO updatePost(PostReqDTO.UpdatePostRequestDTO reqDTO, Long postId);
    PostResDTO.DeletePostResponseDTO deletePost(Long postId);
    String postLike(Long postId, PrincipalDetails principalDetails);
    String deleteLike(Long postId, PrincipalDetails principalDetails);
    String postScrap(Long postId, PrincipalDetails principalDetails);
    String deleteScrap(Long postId, PrincipalDetails principalDetails);
}
