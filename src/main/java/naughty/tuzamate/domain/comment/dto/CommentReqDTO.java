package naughty.tuzamate.domain.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class CommentReqDTO {

    @Builder
    public record CreateCommentRequestDTO(
            String content,
            Long parentId // null 인 경우 댓글, null 이 아닌 경우 대댓글
    ){}

    @Builder
    public record UpdateCommentRequestDTO(
            String content
    ){}
}
