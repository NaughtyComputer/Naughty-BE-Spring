package naughty.tuzamate.domain.stock.error.exception;

import naughty.tuzamate.global.error.BaseErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;

public class StockCustomException extends CustomException {

    public StockCustomException(BaseErrorCode code) {
        super(code);
    }
}
