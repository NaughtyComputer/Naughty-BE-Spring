package naughty.tuzamate.domain.post.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PostErrorCode implements BaseErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST404", "게시글이 존재하지 않습니다."),;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
