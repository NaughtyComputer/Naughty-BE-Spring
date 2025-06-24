package naughty.tuzamate.domain.user.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER40402", "해당 회원이 존재하지 않습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER40901", "회원이 이미 존재합니다."),
    USER_PASSWORD_INCORRECT(HttpStatus.CONFLICT, "USER40101", "패스워드가 올바르지 않습니다"),
    OAUTH_TOKEN_FAIL(HttpStatus.UNAUTHORIZED, "USER40101", "인가코드로 토큰을 가져오는데 실패했습니다."),
    OAUTH_USER_INFO_FAIL(HttpStatus.UNAUTHORIZED, "USER40102", "토큰으로 사용자 정보를 가져오는 데 실패했습니다."),
    UNSUPPORTED_OAUTH_TYPE(HttpStatus.BAD_REQUEST, "USER400", "지원하지 않는 소셜 로그인입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "USER401", "인증되지 않은 사용자입니다.")
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;
}
