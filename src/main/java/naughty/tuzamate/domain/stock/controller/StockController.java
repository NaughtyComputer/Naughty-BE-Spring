package naughty.tuzamate.domain.stock.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naughty.tuzamate.domain.stock.error.StockErrorCode;
import naughty.tuzamate.domain.stock.service.StockService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService stockService;

    @PostMapping("/post-stock-codes")
    @Tag(name = "코스피, 코스닥, 나스닥 주식 코드를 DB에 저장")
    public CustomResponse<?> saveStockCodes() {

        try {
            stockService.codeSaveProcess();
            return CustomResponse.onSuccess("주식 코드 저장 완료");
        } catch (Exception e) {
            log.error("주식 코드 저장 중 오류 발생: {}", e.getMessage(), e);
            return CustomResponse.onFail(StockErrorCode.STOCK_CODE_SAVE_ERROR);
        }
    }
}
