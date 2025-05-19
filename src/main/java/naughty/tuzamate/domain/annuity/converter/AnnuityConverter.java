package naughty.tuzamate.domain.annuity.converter;

import naughty.tuzamate.domain.annuity.dto.AnnuityRequestDTO;
import naughty.tuzamate.domain.annuity.entity.Annuity;

public class AnnuityConverter {

    // AnnuityProductDTO -> Annuity entity
    public static Annuity toEntity(AnnuityRequestDTO.AnnuityProductDTO reqDTO) {
        return Annuity.builder()
                .korCoNm(reqDTO.kor_co_nm())
                .finPrdtNm(reqDTO.fin_prdt_nm())
                .pnsnKindNm(reqDTO.pnsn_kind_nm())
                .prdtTypeNm(reqDTO.prdt_type_nm())
                .avgPrftRate(reqDTO.avg_prft_rate())
                .guarRate(reqDTO.guar_rate())
                .btrmPrftRate1(reqDTO.btrm_prft_rate_1())
                .joinWay(reqDTO.join_way())
                .saleCo(reqDTO.sale_co())
                .saleStrtDay(reqDTO.sale_strt_day())
                .build();
    }
}
