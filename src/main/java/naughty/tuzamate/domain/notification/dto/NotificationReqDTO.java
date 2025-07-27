package naughty.tuzamate.domain.notification.dto;

import lombok.Builder;

public class NotificationReqDTO {

    @Builder
    public record NotificationCursorReqDTO(
       Long cursor,
       int size
    ) {}
}
