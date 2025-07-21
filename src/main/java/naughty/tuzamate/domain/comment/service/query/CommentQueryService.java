package naughty.tuzamate.domain.comment.service.query;

import naughty.tuzamate.domain.comment.dto.CommentResDTO;

public interface CommentQueryService {
    CommentResDTO.CommentPreviewDTO getComment(Long commentId);
    CommentResDTO.CommentPreviewListDTO getCommentList(Long postId, Long cursor, int size);
}
