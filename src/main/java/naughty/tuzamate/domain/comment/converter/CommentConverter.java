package naughty.tuzamate.domain.comment.converter;

import naughty.tuzamate.domain.comment.dto.CommentReqDTO;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;
import naughty.tuzamate.domain.comment.entity.Comment;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    // CreateCommentRequestDTO -> Comment Entity
    public static Comment toComment(CommentReqDTO.CreateCommentRequestDTO reqDTO, User user, Post post, Comment comment) {
        return Comment.builder()
                .content(reqDTO.content())
                .user(user)
                .post(post)
                .parent(comment)
                .build();
    }

    // Comment Entity -> CreateCommentRequestDTO
    public static CommentResDTO.CreateCommentResponseDTO toCreateCommentResponseDTO(Comment comment) {
        return CommentResDTO.CreateCommentResponseDTO.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // Comment Entity -> CommentPreviewDTO
    public static CommentResDTO.CommentPreviewDTO toCommentPreviewDTO(Comment comment) {
        return CommentResDTO.CommentPreviewDTO.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .parentId(comment.getParent().getId())
                .content(comment.getContent())
                .writerName(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    // List<Comment> -> CommentPreviewListDTO
    public static CommentResDTO.CommentPreviewListDTO toCommentPreviewListDTO(List<Comment> comments) {

        List<CommentResDTO.CommentPreviewDTO> commentPreviewDTOS = comments.stream()
                .map(CommentConverter::toCommentPreviewDTO).collect(Collectors.toList());

        return CommentResDTO.CommentPreviewListDTO.builder()
                .commentPreviewListDTO(commentPreviewDTOS)
                .build();
    }

    // Comment Entity -> UpdateCommentResponseDTO
    public static CommentResDTO.UpdateCommentResponseDTO toUpdateCommentResponseDTO(Comment comment) {
        return CommentResDTO.UpdateCommentResponseDTO.builder()
                .id(comment.getId())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    // Long commentId -> DeleteCommentResponseDTO
    public static CommentResDTO.DeleteCommentResponseDTO toDeleteCommentResponseDTO(Long commentId) {
        return CommentResDTO.DeleteCommentResponseDTO.builder()
                .id(commentId)
                .deletedAt(LocalDateTime.now())
                .build();
    }
}
