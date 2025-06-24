package naughty.tuzamate.domain.annuity.dto;

import lombok.Builder;

import java.util.List;

public class AnnuityRequestDTO {

    @Builder
    public record AnnuityProductDTO(
            String kor_co_nm,
            String fin_prdt_nm,
            String pnsn_kind_nm,
            String prdt_type_nm,
            Double avg_prft_rate,
            Double guar_rate,
            Double btrm_prft_rate_1,
            String join_way,
            String sale_co,
            String sale_strt_day
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