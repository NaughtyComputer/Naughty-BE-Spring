package naughty.tuzamate.domain.post.service.query;

import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.enums.BoardType;

public interface PostQueryService {

    PostResDTO.PostPreviewDTO getPost(Long postId);
    PostResDTO.PostPreviewListDTO getPostList(BoardType boardType, Long cursor, int size);
}
