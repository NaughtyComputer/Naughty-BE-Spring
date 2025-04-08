package naughty.tuzamate.auth.jwt.error.exception;

import lombok.Getter;
import naughty.tuzamate.auth.jwt.error.JwtErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;

@Getter
public class AuthException extends CustomException {

    public AuthException(JwtErrorCode code) {
        super(code);
    }
}
