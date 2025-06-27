package naughty.tuzamate.auth.hantu.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum HantuErrorCode implements BaseErrorCode {

    TOKEN_REFRESH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "HANTU50001", "한국투자증권 액세스 키 갱신에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
