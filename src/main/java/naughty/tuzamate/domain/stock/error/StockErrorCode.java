package naughty.tuzamate.domain.stock.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import naughty.tuzamate.global.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StockErrorCode implements BaseErrorCode {

    STOCK_CODE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "STOCK50001", "주식 코드 저장에 실패했습니다."),
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;
}
