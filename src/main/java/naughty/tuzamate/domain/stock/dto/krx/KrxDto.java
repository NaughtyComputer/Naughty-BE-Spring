package naughty.tuzamate.domain.stock.dto.krx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import naughty.tuzamate.domain.stock.dto.StockInfoDto;
import naughty.tuzamate.domain.stock.entity.KrxStockInfo;

public class KrxDto {


    @Getter
    @Setter
    public static class InquireDto {

        @JsonProperty("stck_prpr")
        private String stckPrpr;     // 주식 현재가

        private String per;           // PER

        private String pbr;           // PBR

        @JsonProperty("stck_shrn_iscd")
        private String stckShrnIscd; // 주식 단축 종목코드

        @JsonProperty("bstp_kor_isnm")
        private String bstpKorIsnm; // 업종 한글 종목명
    }

    @Getter
    @Setter
    public static class FinancialDto {

        @JsonProperty("bsop_prfi_inrt")
        private String bsopPrfiInrt; // 영업 이익 증가율

        private String eps; // EPS

        @JsonProperty("roe_val")
        private String roeVal; // ROE 값
    }


    @Getter
    public static class KrxStockInfoDto {
        private String stck_prpr;     // 주식 현재가
        private String per;     // PER
        private String pbr;     // PBR
        private String stck_shrn_iscd;     // 주식 단축 종목코드
        private String bstp_kor_isnm; // 업종 한글 종목명
        private String bsop_prfi_inrt; // 영업 이익 증가율
        private String roe_val; // ROE 값
        private String eps; // EPS
        private String prdt_abrv_name; // 상품 약어명

        public KrxStockInfo toEntity(KrxDto.InquireDto inquireDto,
                                     KrxDto.FinancialDto financialDto,
                                     StockInfoDto.InfoDto stockInfoDto) {
            return KrxStockInfo.builder()
                    .stckShrnIscd(inquireDto.getStckShrnIscd())
                    .per(inquireDto.getPer())
                    .pbr(inquireDto.getPbr())
                    .stckPrpr(inquireDto.getStckPrpr())
                    .bstpKorIsnm(inquireDto.getBstpKorIsnm())
                    .bsopPrfiInrt(financialDto.getBsopPrfiInrt())
                    .roeVal(financialDto.getRoeVal())
                    .eps(financialDto.getEps())
                    .prdtAbrvName(stockInfoDto.getPrdtAbrvName())
                    .build();
        }
    }
}
