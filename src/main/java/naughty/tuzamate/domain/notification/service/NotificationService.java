package naughty.tuzamate.domain.notification.service;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.notification.code.NotificationErrorCode;
import naughty.tuzamate.domain.notification.converter.NotificationConverter;
import naughty.tuzamate.domain.notification.dto.NotificationResDTO;
import naughty.tuzamate.domain.notification.entity.Notification;
import naughty.tuzamate.domain.notification.repository.NotificationRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
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

        notification.updateIsRead();

        return NotificationConverter.toNotificationDTO(notification);
    }

    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, userId)
                .orElseThrow(() -> new CustomException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
    }

}
