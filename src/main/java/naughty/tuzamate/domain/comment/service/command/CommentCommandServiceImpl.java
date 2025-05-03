package naughty.tuzamate.domain.comment.service.command;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.comment.converter.CommentConverter;
import naughty.tuzamate.domain.comment.dto.CommentReqDTO;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;
import naughty.tuzamate.domain.comment.entity.Comment;
import naughty.tuzamate.domain.comment.repository.CommentRepository;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.repository.PostRepository;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public CommentResDTO.CreateCommentResponseDTO createComment(CommentReqDTO.CreateCommentRequestDTO reqDTO) {
        User user = userRepository.findById(reqDTO.userId())
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        Post post = postRepository.findById(reqDTO.postId())
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        Comment parent = null;

        if (reqDTO.parentId() != null) {
            parent = commentRepository.findById(reqDTO.parentId())
                    .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
        }

        Comment comment = CommentConverter.toComment(reqDTO, user, post, parent);

        commentRepository.save(comment);

        return CommentConverter.toCreateCommentResponseDTO(comment);
    }

    @Override
    public CommentResDTO.UpdateCommentResponseDTO updateComment(CommentReqDTO.UpdateCommentRequestDTO reqDTO, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        comment.updateContent(reqDTO.content());

        return CommentConverter.toUpdateCommentResponseDTO(comment);
    }

    @Override
    public CommentResDTO.DeleteCommentResponseDTO deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);

        return CommentConverter.toDeleteCommentResponseDTO(commentId);
    }
}
