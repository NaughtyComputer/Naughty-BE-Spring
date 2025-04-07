package naughty.tuzamate.domain.post.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import naughty.tuzamate.domain.post.dto.PostReqDTO;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostConverter {

    // CreatePostRequestDTO -> Post Entity
    public static Post toPost(PostReqDTO.CreatePostRequestDTO reqDTO) {
        return Post.builder()
                .title(reqDTO.title())
                .content(reqDTO.content())
                .likeNum(0L)
                .build();
    }

    // Post Entity -> CreatePostResponseDTO
    public static PostResDTO.CreatePostResponseDTO toCreatePostResponseDTO(Post post) {
        return PostResDTO.CreatePostResponseDTO.builder()
                .id(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }

    // Post Entity -> PostPreviewDTO
    public static PostResDTO.PostPreviewDTO toPostPreviewDTO(Post post) {
        return PostResDTO.PostPreviewDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeNum(post.getLikeNum())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // Post Entities -> PostPreviewListDTO
    public static PostResDTO.PostPreviewListDTO toPostPreviewListDTO(List<Post> posts) {
        List<PostResDTO.PostPreviewDTO> previewDTOList = posts.stream()
                .map(PostConverter::toPostPreviewDTO)
                .collect(Collectors.toList());

        return PostResDTO.PostPreviewListDTO.builder()
                .postPreviewDTOList(previewDTOList)
                .build();
    }

    // Post Entity -> UpdatePostResponseDTO
    public static PostResDTO.UpdatePostResponseDTO toUpdatePostResponseDTO(Post post) {
        return PostResDTO.UpdatePostResponseDTO.builder()
                .id(post.getId())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // Post Entity(postId) -> DeletePostResponseDTO
    public static PostResDTO.DeletePostResponseDTO toDeletePostResponseDTO(Long postId) {
        return PostResDTO.DeletePostResponseDTO.builder()
                .id(postId)
                //.deletedAt()
                .build();
    }
}
