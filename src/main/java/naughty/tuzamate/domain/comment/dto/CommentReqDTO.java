package naughty.tuzamate.domain.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class CommentReqDTO {

    @Builder
    public record CreateCommentRequestDTO(
            String content,
            Long userId,
            Long postId,
            Long parentId
    ){}

    @Builder
    public record UpdateCommentRequestDTO(
            String content
    ){}
}
