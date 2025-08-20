package naughty.tuzamate.domain.post.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.success.BaseSuccessCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PostSuccessCode implements BaseSuccessCode {

    POST_OK(HttpStatus.OK, "200", "게시글 조회에 성공했습니다."),

    POST_CREATED(HttpStatus.CREATED, "201", "게시글이 생성되었습니다."),

    DELETED(HttpStatus.NO_CONTENT, "204", "성공적으로 삭제되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
