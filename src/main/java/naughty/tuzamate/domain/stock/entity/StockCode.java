package naughty.tuzamate.domain.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 한국 주식 코드 엔티티 (코스피, 코스닥)
public class StockCode {

    @Id
    private String code;

    public static StockCode of(String code) {
        return new StockCode(code);
    }

}
