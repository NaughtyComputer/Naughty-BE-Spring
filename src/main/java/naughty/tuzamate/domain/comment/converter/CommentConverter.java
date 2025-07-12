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
    public static Comment toComment(CommentReqDTO.CreateCommentRequestDTO reqDTO, User user, Post post, Comment parent) {
        Comment comment =  Comment.builder()
                .content(reqDTO.content())
                .user(user)
                .post(post)
                .parent(parent)
                .build();

        post.addComment(comment);

        return comment;
    }

    // Comment Entity -> CreateCommentRequestDTO
    public static CommentResDTO.CreateCommentResponseDTO toCreateCommentResponseDTO(Comment comment) {
        return CommentResDTO.CreateCommentResponseDTO.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // Comment Entity -> CommentPreviewDTO
    public static CommentResDTO.CommentPreviewDTO toCommentPreviewDTO(Comment comment, List<Comment> children) {
        return CommentResDTO.CommentPreviewDTO.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .content(comment.getContent())
                .writerName(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .children(children.stream()
                        .map(child -> toCommentPreviewDTO(child, List.of()))
                        .collect(Collectors.toList()))
                .build();
    }

    // List<Comment> -> CommentPreviewListDTO
    public static CommentResDTO.CommentPreviewListDTO toCommentPreviewListDTO(
            List<CommentResDTO.CommentPreviewDTO> previewList, boolean hasNext, Long nextCursor) {

        return CommentResDTO.CommentPreviewListDTO.builder()
                .commentPreviewListDTO(previewList)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
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
