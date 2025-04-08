package naughty.tuzamate.domain.post.service.query;

import naughty.tuzamate.domain.post.dto.PostReqDTO;
import naughty.tuzamate.domain.post.dto.PostResDTO;

public interface PostQueryService {

    PostResDTO.PostPreviewDTO getPost(Long postId);
    PostResDTO.PostPreviewListDTO getPostList();
}
