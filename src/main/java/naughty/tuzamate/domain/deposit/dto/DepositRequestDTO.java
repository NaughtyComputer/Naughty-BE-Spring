package naughty.tuzamate.domain.deposit.dto;

import lombok.Builder;

import java.util.List;

public class DepositRequestDTO {

    @Builder
    public record DepositProductDTO(
            String kor_co_nm,
            String fin_prdt_nm,
            String join_way,
            String join_member,
            String mtrt_int,
            String spcl_cnd,
            String etc_note
    ) {}

    @Builder
    public record ResultWrapperDTO(
       List<DepositProductDTO> baseList
    ) {}

    @Builder
    public record ApiResultDTO (
        ResultWrapperDTO result
    ) {}

}
