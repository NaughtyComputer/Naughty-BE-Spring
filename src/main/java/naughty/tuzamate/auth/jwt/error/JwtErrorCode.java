package naughty.tuzamate.auth.jwt.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum JwtErrorCode implements BaseErrorCode {

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN4001", "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN4002", "유효한 토큰 형식이 아닙니다."),
    JWT_BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "JWT400", "잘못된 형식의 토큰입니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

}
