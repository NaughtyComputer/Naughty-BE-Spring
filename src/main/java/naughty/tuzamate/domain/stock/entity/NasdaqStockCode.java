package naughty.tuzamate.domain.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
// 미국 주식 코드 엔티티 (나스닥)
public class NasdaqStockCode {

    @Id
    private String code;

    public static NasdaqStockCode of(String code) {
        return new NasdaqStockCode(code);
    }

}
