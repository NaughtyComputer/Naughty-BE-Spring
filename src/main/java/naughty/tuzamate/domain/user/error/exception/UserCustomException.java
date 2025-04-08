package naughty.tuzamate.domain.user.error.exception;

import naughty.tuzamate.global.error.BaseErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;

public class UserCustomException extends CustomException {

    public UserCustomException(BaseErrorCode code) {
        super(code);
    }
}
