package naughty.tuzamate.domain.profile.error.exception;

import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;

@Getter
public class ProfileCustomException extends CustomException {

    public ProfileCustomException(BaseErrorCode code) {
        super(code);
    }
}
