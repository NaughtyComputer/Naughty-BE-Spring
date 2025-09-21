package naughty.tuzamate.domain.pushToken.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PushTokenErrorCode implements BaseErrorCode {
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN40401", "해당 토큰이 존재하지 않습니다."),
    TOKEN_NOT_OWNER(HttpStatus.BAD_REQUEST, "TOKEN402", "토큰 소유자가 아닙니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}