package naughty.tuzamate.domain.comment.service.query;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.comment.converter.CommentConverter;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;
import naughty.tuzamate.domain.comment.entity.Comment;
import naughty.tuzamate.domain.comment.repository.CommentRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentRepository commentRepository;

    @Override
    public CommentResDTO.CommentPreviewDTO getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        return CommentConverter.toCommentPreviewDTO(comment);
    }

    @Override
    public CommentResDTO.CommentPreviewListDTO getCommentList(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return CommentConverter.toCommentPreviewListDTO(comments);
    }
}
