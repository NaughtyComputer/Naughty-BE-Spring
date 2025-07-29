package naughty.tuzamate.domain.notification.converter;

import naughty.tuzamate.domain.notification.dto.NotificationResDTO;
import naughty.tuzamate.domain.notification.entity.Notification;

public class NotificationConverter {

    // Notification Entity -> toNotificationReqDTO
    public static NotificationResDTO.toNotificationResDTO toNotificationDTO(Notification notification) {
        return NotificationResDTO.toNotificationResDTO.builder()
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .targetId(notification.getTargetId())
                .build();
    }
}
