package naughty.tuzamate.domain.comment.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT404", "댓글이 존재하지 않습니다."),;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
