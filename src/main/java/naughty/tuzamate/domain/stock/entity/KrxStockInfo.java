package naughty.tuzamate.domain.stock.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class KrxStockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "krx_stock_info_seq")
    @SequenceGenerator(
            name = "krx_stock_info_seq",
            sequenceName = "krx_stock_info_seq",
            allocationSize = 50 // 시퀀스 한 번에 50개 id 확보
    )
    private Long id;

    private String stckShrnIscd; // 주식 단축 종목코드

    private String per;

    private String pbr;

    private String stckPrpr; // 주식 현재가

    private String bstpKorIsnm; // 업종 한글 종목명

    private String eps; // 주당 순이익 (Earnings Per Share)

    private String prdtAbrvName; // 상품 약어명
}
