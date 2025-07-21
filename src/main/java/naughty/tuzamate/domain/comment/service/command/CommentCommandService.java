package naughty.tuzamate.domain.comment.service.command;

import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.comment.dto.CommentReqDTO;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;

public interface CommentCommandService {
    CommentResDTO.CreateCommentResponseDTO createComment(
            CommentReqDTO.CreateCommentRequestDTO reqDTO, Long postId, PrincipalDetails principalDetails);

    CommentResDTO.UpdateCommentResponseDTO updateComment(
            CommentReqDTO.UpdateCommentRequestDTO reqDTO, Long commentId);

    CommentResDTO.DeleteCommentResponseDTO deleteComment(Long commentId);
}
