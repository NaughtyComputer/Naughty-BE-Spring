package naughty.tuzamate.domain.stock.strategy;

import naughty.tuzamate.domain.stock.dto.krx.KrxDto;
import naughty.tuzamate.domain.stock.dto.nasdaq.NasdaqDto;

public interface FilterStrategy {

    boolean shouldSkipNasdaq(NasdaqDto.NasdaqInfoDto nasdaqInfoDto);

    boolean shouldSkipKrx(KrxDto.InquireDto inquireDto,
                       KrxDto.FinancialDto financialDto);
}
