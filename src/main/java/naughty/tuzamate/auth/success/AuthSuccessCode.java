package naughty.tuzamate.auth.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.success.BaseSuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements BaseSuccessCode {

    AUTH_SUCCESS_CODE(HttpStatus.ACCEPTED, "AUTH200", "인가 코드 발급이 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;


}
