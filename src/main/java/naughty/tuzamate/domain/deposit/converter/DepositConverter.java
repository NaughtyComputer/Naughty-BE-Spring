package naughty.tuzamate.domain.deposit.converter;

import naughty.tuzamate.domain.deposit.dto.DepositRequestDTO;
import naughty.tuzamate.domain.deposit.entity.Deposit;

public class DepositConverter {

    // DepositProductDTO -> Deposit entity
    public static Deposit toEntity(DepositRequestDTO.DepositProductDTO reqDTO) {
        return Deposit.builder()
                .korCoNm(reqDTO.kor_co_nm())
                .finPrdtNm(reqDTO.fin_prdt_nm())
                .joinWay(reqDTO.join_way())
                .joinMember(reqDTO.join_member())
                .mtrtInt(reqDTO.mtrt_int())
                .spclCnd(reqDTO.spcl_cnd())
                .etcNote(reqDTO.etc_note())
                .build();
    }
}
