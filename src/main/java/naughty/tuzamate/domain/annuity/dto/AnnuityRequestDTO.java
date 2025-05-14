package naughty.tuzamate.domain.annuity.dto;

import lombok.Builder;

import java.util.List;

public class AnnuityRequestDTO {

    @Builder
    public record AnnuityProductDTO(
            String kor_co_nm,
            String fin_prdt_nm,
            String pnsnKindNm,
            String prdtTypeNm,
            Double avgPrftRate,
            Double guarRate,
            Double btrmPrftRate1,
            String joinWay,
            String saleCo,
            String saleStrtDay
    ) {}

    @Builder
    public record ResultWrapperDTO(
            List<AnnuityProductDTO> baseList
    ) {}

    @Builder
    public record ApiResultDTO (
            ResultWrapperDTO result
    ) {}
}