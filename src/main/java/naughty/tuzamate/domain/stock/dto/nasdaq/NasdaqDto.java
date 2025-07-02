package naughty.tuzamate.domain.stock.dto.nasdaq;

import lombok.Getter;
import lombok.Setter;
import naughty.tuzamate.domain.stock.dto.StockInfoDto;
import naughty.tuzamate.domain.stock.entity.NasdaqStockInfo;

public class NasdaqDto {

  @Setter
  @Getter
  public static class NasdaqInfoDto {

    private String code;
    private String perx; // PER
    private String pbrx; // PBR
    private String epsx; // EPS
    private String e_icod; // 업종 섹터
    private String last; // 현재가

    public NasdaqStockInfo toEntity(NasdaqDto.NasdaqInfoDto nasdaqInfoDto, StockInfoDto.InfoDto stockInfoDto) {
      return NasdaqStockInfo.builder()
              .code(nasdaqInfoDto.code)
              .perx(nasdaqInfoDto.perx)
              .pbrx(nasdaqInfoDto.pbrx)
              .epsx(nasdaqInfoDto.epsx)
              .eIcod(nasdaqInfoDto.e_icod)
              .last(nasdaqInfoDto.last)
              .prdtAbrvName(stockInfoDto.getPrdtAbrvName())
              .build();
    }

  }
}
