package naughty.tuzamate.auth.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.success.BaseSuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements BaseSuccessCode {

    AUTH_SUCCESS_CODE(HttpStatus.ACCEPTED, "AUTH200", "인가 코드 발급이 성공했습니다."),
    LOGOUT_SUCCESS_CODE(HttpStatus.OK, "AUTH200", "로그아웃이 되었습니다."),

    ACCESS_TOKEN_REISSUE_SUCCESS_CODE(HttpStatus.OK, "AUTH202", "액세스 토큰 재발급이 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;


}
