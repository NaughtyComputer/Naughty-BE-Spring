package naughty.tuzamate.domain.savings.converter;

import naughty.tuzamate.domain.savings.dto.SavingsRequestDTO;
import naughty.tuzamate.domain.savings.entity.Savings;

public class SavingsConverter {
    // SavingsProductDTO -> Savings entity
    public static Savings toEntity(SavingsRequestDTO.SavingsProductDTO reqDTO) {
        return Savings.builder()
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
