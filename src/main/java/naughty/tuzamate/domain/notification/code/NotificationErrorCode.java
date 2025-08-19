package naughty.tuzamate.domain.notification.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum NotificationErrorCode implements BaseErrorCode {

    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION404", "알림이 존재하지 않습니다."),;

    private final HttpStatus status;
    private final String code;
    private final String message;
}