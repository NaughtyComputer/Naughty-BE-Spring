package naughty.tuzamate.domain.post.service.query;

import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.enums.BoardType;

public interface PostQueryService {

    PostResDTO.PostDTO getPost(Long postId, PrincipalDetails principalDetails);
    PostResDTO.PostPreviewListDTO getPostList(BoardType boardType, Long cursor, int size);
}
