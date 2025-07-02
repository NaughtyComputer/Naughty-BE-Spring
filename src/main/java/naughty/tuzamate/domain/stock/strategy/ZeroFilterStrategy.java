package naughty.tuzamate.domain.stock.strategy;

import naughty.tuzamate.domain.stock.dto.krx.KrxDto;
import naughty.tuzamate.domain.stock.dto.nasdaq.NasdaqDto;
import org.springframework.stereotype.Component;

@Component
public class ZeroFilterStrategy implements FilterStrategy {

    @Override
    public boolean shouldSkipNasdaq(NasdaqDto.NasdaqInfoDto dto) {
        return "0.00".equals(dto.getPerx()) ||
                "0.00".equals(dto.getPbrx()) ||
                "0.00".equals(dto.getEpsx());
    }

    @Override
    public boolean shouldSkipKrx(KrxDto.InquireDto inquireDto, KrxDto.FinancialDto financialDto) {
        return "0.00".equals(inquireDto.getPer()) ||
                "0.00".equals(inquireDto.getPbr()) ||
                "0.00".equals(financialDto.getEps());
    }

}
