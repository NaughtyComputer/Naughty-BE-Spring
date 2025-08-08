package naughty.tuzamate.domain.profile.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ProfileErrorCode implements BaseErrorCode {

    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE40402", "스크랩을 찾을 수 없습니다."),
    INVALID_PROFILE_UPDATE(HttpStatus.BAD_REQUEST, "PROFILE40001", "프로필 업데이트에 실패했습니다."),
    MISMATCHED_NICKNAME(HttpStatus.BAD_REQUEST, "PROFILE40002", "닉네임이 일치하지 않습니다."),;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
