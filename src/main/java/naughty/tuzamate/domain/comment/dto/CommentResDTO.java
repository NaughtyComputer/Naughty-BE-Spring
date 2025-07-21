package naughty.tuzamate.domain.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResDTO {

    @Builder
    public record CreateCommentResponseDTO(
            Long id,
            LocalDateTime createdAt
    ){}

    @Builder
    public record CommentPreviewDTO(
            Long id,
            Long postId,
            Long parentId,       // optional
            String content,
            String writerName,   // optional
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<CommentPreviewDTO> children
    ) {}

    @Builder
    public record CommentPreviewListDTO(
            List<CommentPreviewDTO> commentPreviewListDTO,
            boolean hasNext,
            Long nextCursor
    ){}

    @Builder
    public record UpdateCommentResponseDTO(
            Long id,
            LocalDateTime updatedAt
    ){}

    @Builder
    public record DeleteCommentResponseDTO(
            Long id,
            LocalDateTime deletedAt
    ){}
}
