package naughty.tuzamate.domain.comment.service.command;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.comment.code.CommentErrorCode;
import naughty.tuzamate.domain.comment.converter.CommentConverter;
import naughty.tuzamate.domain.comment.dto.CommentReqDTO;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;
import naughty.tuzamate.domain.comment.entity.Comment;
import naughty.tuzamate.domain.comment.repository.CommentRepository;
import naughty.tuzamate.domain.notification.entity.Notification;
import naughty.tuzamate.domain.notification.service.NotificationService;
import naughty.tuzamate.domain.post.code.PostErrorCode;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.repository.PostRepository;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    @Override
    public CommentResDTO.CreateCommentResponseDTO createComment(
            CommentReqDTO.CreateCommentRequestDTO reqDTO, Long postId, PrincipalDetails principalDetails
    ) {
        User commentWriter = userRepository.findById(principalDetails.getId())
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        Comment parent = null;

        if (reqDTO.parentId() != null) {
            parent = commentRepository.findById(reqDTO.parentId())
                    .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
        }

        Comment comment = CommentConverter.toComment(reqDTO, commentWriter, post, parent);

        commentRepository.save(comment);

        // 알림 대상 계산
        Set<Long> receivers = new LinkedHashSet<>();

        Long postWriterId = post.getUser().getId();
        Long parentWriterId = parent != null ? parent.getUser().getId() : null;
        Long writerId = commentWriter.getId();

        if (!postWriterId.equals(writerId)) {
            receivers.add(postWriterId);
        }
        if (parentWriterId != null && !parentWriterId.equals(writerId) && !parentWriterId.equals(postWriterId)) {
            receivers.add(parentWriterId);
        }

        // 알림 저장 + (커밋 후) FCM 발송
        for (Long receiverId : receivers) {
            Notification notification = Notification.builder()
                    .receiver(User.builder().id(receiverId).build())
                    .title("새 댓글이 달렸습니다")
                    .content(comment.getContent())
                    .targetId(post.getId())
                    .isRead(false)
                    .build();

            Map<String, String> data = Map.of(
                    "type", "COMMENT",
                    "postId", String.valueOf(post.getId()),
                    "commentId", String.valueOf(comment.getId()),
                    "deeplink", "myapp://post/" + post.getId() + "?commentId=" + comment.getId()
            );

            notificationService.saveAndDispatch(
                    notification, receiverId,
                    "새 댓글 알림", comment.getContent(),
                    data, true, "OPEN_POST" // 클릭 액션 키(프론트에 맞추기)
            );
        }

        return CommentConverter.toCreateCommentResponseDTO(comment);
    }

    @Override
    public CommentResDTO.UpdateCommentResponseDTO updateComment(
            CommentReqDTO.UpdateCommentRequestDTO reqDTO,
            Long commentId,
            PrincipalDetails principalDetails
    ) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(principalDetails.getUser().getId())) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403); // 권한 없음
        }

        comment.updateContent(reqDTO.content());

        return CommentConverter.toUpdateCommentResponseDTO(comment);
    }

    @Override
    public CommentResDTO.DeleteCommentResponseDTO deleteComment(Long commentId, PrincipalDetails principalDetails) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(principalDetails.getUser().getId())) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403);
        }

        commentRepository.delete(comment);

        return CommentConverter.toDeleteCommentResponseDTO(commentId);
    }
}
