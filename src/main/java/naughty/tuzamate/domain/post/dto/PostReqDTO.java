package naughty.tuzamate.domain.post.dto;

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
