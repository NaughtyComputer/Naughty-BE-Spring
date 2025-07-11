package naughty.tuzamate.domain.post.dto;

import naughty.tuzamate.domain.post.enums.BoardType;

public class PostReqDTO {

    public record CreatePostRequestDTO(
            String title,
            String content
    ){
    }

    public record UpdatePostRequestDTO(
            String title,
            String content
    ){
    }
}
