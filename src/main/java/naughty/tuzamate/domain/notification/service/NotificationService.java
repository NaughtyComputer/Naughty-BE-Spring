package naughty.tuzamate.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naughty.tuzamate.domain.notification.code.NotificationErrorCode;
import naughty.tuzamate.domain.notification.converter.NotificationConverter;
import naughty.tuzamate.domain.notification.dto.NotificationResDTO;
import naughty.tuzamate.domain.notification.entity.Notification;
import naughty.tuzamate.domain.notification.repository.NotificationRepository;
import naughty.tuzamate.domain.pushToken.FcmSender;
import naughty.tuzamate.domain.pushToken.repository.PushTokenRepository;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PushTokenRepository pushTokenRepository;
    private final FcmSender fcmSender;
    private final PlatformTransactionManager txManager;

    // 알림 저장 + 트랜잭션 커밋 후 FCM 발송
    @Transactional
    public void saveAndDispatch(Notification notification, Long receiverId,
                                String title, String body, Map<String,String> data,
                                boolean highPriority, String clickAction) {

        // 알림 저장
        notificationRepository.save(notification);

        // 커밋 이후 작업 등록
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                List<String> tokens = pushTokenRepository.findActiveTokensByUserId(receiverId);
                if (tokens.isEmpty()) {
                    log.info("No active tokens for receiverId={}", receiverId);
                    return;
                }

                try {
                    FcmSender.BatchResult br = fcmSender.sendToTokens(
                            tokens, title, body, data, highPriority, clickAction
                    );

                    // 커밋 이후, 영구 실패 토큰만 비활성화
                    if (!br.failedTokens().isEmpty()) {
                        deactivateTokensRequiresNew(br.failedTokens());
                        log.info("Deactivated {} tokens (receiverId={})",
                                br.failedTokens().size(), receiverId);
                    }

                    log.info("FCM sent: success={}, failure={}, receiverId={}",
                            br.success(), br.failure(), receiverId);

                } catch (Exception e) {
                    // 커밋 이후 예외이므로 본 트랜잭션에 영향 없음
                    log.warn("FCM send failed afterCommit. receiverId={}", receiverId, e);
                }
            }
        });
    }

    /**
     * REQUIRES_NEW 트랜잭션으로 토큰 비활성화(벌크).
     * (self-invocation 회피: @Transactional 사용 대신 프로그래매틱 트랜잭션)
     */
    private void deactivateTokensRequiresNew(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) return;

        List<String> distinct = tokens.stream().distinct().collect(Collectors.toList());

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        TransactionStatus status = txManager.getTransaction(def);
        try {
            pushTokenRepository.bulkDeactivateByTokens(distinct);
            txManager.commit(status);
        } catch (Exception ex) {
            txManager.rollback(status);
            log.warn("Failed to deactivate tokens in REQUIRES_NEW tx (size={})", distinct.size(), ex);
        }
    }

    public NotificationResDTO.NotificationCursorResDTO getNotificationList(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);

        List<Notification> result = notificationRepository.findByReceiverIdAndCursor(userId, cursor, pageable);

        boolean hasNext = result.size() > size;

        // 초과할 경우 마지막 항목 제거
        if (hasNext) {
            result = result.subList(0, size);
        }

        List<NotificationResDTO.toNotificationResDTO> list = result.stream()
                .map(n -> NotificationResDTO.toNotificationResDTO.builder()
                        .title(n.getTitle())
                        .content(n.getContent())
                        .isRead(n.isRead())
                        .targetId(n.getTargetId())
                        .build())
                .toList();

        Long nextCursor = hasNext ? result.get(result.size() - 1).getId() : null;

        return NotificationResDTO.NotificationCursorResDTO.builder()
                .notifications(list)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    // 단일 알림 조회(이미 읽은 알림을 다시 읽는 경우)
    public NotificationResDTO.toNotificationResDTO getNotification(Long notificationId, Long userId) {
        Notification notification =  notificationRepository.findByIdAndReceiverId(notificationId, userId)
                .orElseThrow(() -> new CustomException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        return NotificationConverter.toNotificationDTO(notification);
    }

    // 단일 알림 조회(아직 읽지 않은 알림을 읽는 경우)
    @Transactional
    public NotificationResDTO.toNotificationResDTO updateIsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, userId)
                .orElseThrow(() -> new CustomException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        notification.markAsRead();

        return NotificationConverter.toNotificationDTO(notification);
    }

    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, userId)
                .orElseThrow(() -> new CustomException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
    }
}
