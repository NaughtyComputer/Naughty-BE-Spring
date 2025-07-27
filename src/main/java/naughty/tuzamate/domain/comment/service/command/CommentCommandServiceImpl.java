package naughty.tuzamate.domain.comment.service.command;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.comment.converter.CommentConverter;
import naughty.tuzamate.domain.comment.dto.CommentReqDTO;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;
import naughty.tuzamate.domain.comment.entity.Comment;
import naughty.tuzamate.domain.comment.repository.CommentRepository;
import naughty.tuzamate.domain.comment.service.FCMService;
import naughty.tuzamate.domain.notification.entity.Notification;
import naughty.tuzamate.domain.notification.service.NotificationService;
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
    private final FCMService fcmService;
    private final NotificationService notificationService;

    @Override
    public CommentResDTO.CreateCommentResponseDTO createComment(
            CommentReqDTO.CreateCommentRequestDTO reqDTO, Long postId, PrincipalDetails principalDetails
    ) {
        User commentWriter = userRepository.findById(principalDetails.getId())
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        Comment parent = null;

        if (reqDTO.parentId() != null) {
            parent = commentRepository.findById(reqDTO.parentId())
                    .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
        }

        Comment comment = CommentConverter.toComment(reqDTO, commentWriter, post, parent);

        commentRepository.save(comment);

        // 알림 제공 서비스
        User postWriter = post.getUser();
        User parentWriter = parent != null ? parent.getUser() : null;

        String content = comment.getContent();
        String preview = content.length() >= 15 ? content.substring(0, 15) + "..." : content;
        String title = commentWriter.getNickname() + "님이 댓글을 남겼습니다.";

        // 게시글 작성자가 아닌 사용자가 댓글을 단 경우
        if (parent == null && !postWriter.getId().equals(commentWriter.getId())) {
            fcmService.sendNotification(title, preview, postWriter.getFcmToken());

            Notification notification = Notification.builder()
                    .title(title)
                    .content(preview)
                    .isRead(false)
                    .targetId(postId)
                    .receiver(postWriter)
                    .build();

            notificationService.saveNotification(notification);
        }

        // 댓글 작성자에게 대댓글이 달린 경우(부모 댓글 작성자와 대댓글 작성자가 다른 경우)
        if (parent != null && !parentWriter.getId().equals(commentWriter.getId())) {
            fcmService.sendNotification(title, preview, parentWriter.getFcmToken());

            Notification notification = Notification.builder()
                    .title(title)
                    .content(preview)
                    .isRead(false)
                    .targetId(postId)
                    .receiver(parentWriter)
                    .build();

            notificationService.saveNotification(notification);
        }

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
