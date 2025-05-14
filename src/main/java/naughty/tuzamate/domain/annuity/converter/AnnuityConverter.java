package naughty.tuzamate.domain.annuity.converter;

import naughty.tuzamate.domain.annuity.dto.AnnuityRequestDTO;
import naughty.tuzamate.domain.annuity.entity.Annuity;

public class AnnuityConverter {

    // AnnuityProductDTO -> Annuity entity
    public static Annuity toEntity(AnnuityRequestDTO.AnnuityProductDTO reqDTO) {
        return Annuity.builder()
                .korCoNm(reqDTO.kor_co_nm())
                .finPrdtNm(reqDTO.fin_prdt_nm())
                .pnsnKindNm(reqDTO.pnsnKindNm())
                .prdtTypeNm(reqDTO.prdtTypeNm())
                .avgPrftRate(reqDTO.avgPrftRate())
                .guarRate(reqDTO.guarRate())
                .btrmPrftRate1(reqDTO.btrmPrftRate1())
                .joinWay(reqDTO.joinWay())
                .saleCo(reqDTO.saleCo())
                .saleStrtDay(reqDTO.saleStrtDay())
                .build();
    }
}
