package naughty.tuzamate.domain.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class KrxStockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stckShrnIscd; // 주식 단축 종목코드

    private String per;

    private String pbr;

    private String stckPrpr; // 주식 현재가

    private String bstpKorIsnm; // 업종 한글 종목명

    private String bsopPrfiInrt; // 영업 이익 증가율

    private String roeVal; // ROE 값

    private String eps; // 주당 순이익 (Earnings Per Share)
}
