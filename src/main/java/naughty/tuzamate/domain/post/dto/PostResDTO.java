package naughty.tuzamate.domain.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class PostResDTO {

    @Builder
    public record CreatePostResponseDTO(
            Long id,
            LocalDateTime createdAt
    ){
    }

    @Builder
    public record PostPreviewDTO(
            Long id,
            String title,
            String content,
            Long likeNum,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
    }

    @Builder
    public record PostPreviewListDTO(
            List<PostPreviewDTO> postPreviewDTOList
    ){
    }

    @Builder
    public record UpdatePostResponseDTO(
            Long id,
            LocalDateTime updatedAt
    ){
    }

    @Builder
    public record DeletePostResponseDTO(
            Long id,
            LocalDateTime deletedAt
    ){
    }
}
