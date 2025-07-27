package naughty.tuzamate.domain.notification.dto;

import lombok.Builder;
import naughty.tuzamate.domain.notification.entity.Notification;

import java.util.List;

public class NotificationResDTO {

    @Builder
    public record toNotificationResDTO(
            String title,
            String content,
            boolean isRead,
            Long targetId
    ){}

    @Builder
    public record NotificationCursorResDTO(
            List<NotificationResDTO.toNotificationResDTO> notifications,
            boolean hasNext,
            Long nextCursor
    ) {}
}