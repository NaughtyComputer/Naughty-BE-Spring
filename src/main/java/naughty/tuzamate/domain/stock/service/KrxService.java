package naughty.tuzamate.domain.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naughty.tuzamate.domain.stock.dto.StockInfoDto;
import naughty.tuzamate.domain.stock.dto.krx.KrxDto;
import naughty.tuzamate.domain.stock.entity.KrxStockInfo;
import naughty.tuzamate.domain.stock.entity.StockCode;
import naughty.tuzamate.domain.stock.repository.KrxStockInfoRepository;
import naughty.tuzamate.domain.stock.repository.code.StockCodeRepository;
import naughty.tuzamate.domain.stock.strategy.FilterStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KrxService {

    private final StockCodeRepository stockCodeRepository;
    private final KrxInquireService krxInquireService;
    private final KrxFinancialService krxFinancialService;
    private final KrxStockInfoRepository krxStockInfoRepository;
    private final StockInfoService stockInfoService;
    private final FilterStrategy filterStrategy;
    public void saveKrxStocksInfo() {

        List<StockCode> stockCodeList = stockCodeRepository.findAll();

        krxStockInfoRepository.deleteAllInBatch();

        for (StockCode stockCode : stockCodeList) {
            try {

                Thread.sleep(100);

                // 주식 코드를 이용해 현재가, PER, PBR, 업종 한글 종목명 조회
                KrxDto.InquireDto currentPerPbrOutputDto = krxInquireService.getCurInquireInfo(stockCode.getCode());
                // 주식 코드를 이용해 EPS 값 조회
                KrxDto.FinancialDto currentFinanceOutputDto = krxFinancialService.getCurFinancialInfo(stockCode.getCode());
                StockInfoDto.InfoDto currentKrxStockInfoDto = stockInfoService.getStockInfo(stockCode.getCode(), "300");

               /* log.info("PER: {}", currentPerPbrOutputDto.getPer());
                log.info("EPS: {}", currentFinanceOutputDto.getEps());
                log.info("NAME: {}", currentKrxStockInfoDto.getPrdtAbrvName());*/

                if (filterStrategy.shouldSkipKrx(currentPerPbrOutputDto, currentFinanceOutputDto)) {
                    log.info("PER or PBR or EPS is zero: {}", stockCode.getCode());
                    continue;
                }

                KrxDto.KrxStockInfoDto stockInfoDto = new KrxDto.KrxStockInfoDto();

                KrxStockInfo stockInfo = stockInfoDto.toEntity(
                        currentPerPbrOutputDto,
                        currentFinanceOutputDto,
                        currentKrxStockInfoDto
                );

                krxStockInfoRepository.save(stockInfo);

                log.info("Saved stocks is : {}", stockCode.getCode());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 현재 스레드 인터럽트 상태 복구
                log.info("Thread Interrupted : {}", e.getMessage());
                break;
            } catch (Exception e) {
                log.info("Error stock code is {} : {} and pass!", stockCode.getCode(), e.getMessage());
            }
        }
    }
}
