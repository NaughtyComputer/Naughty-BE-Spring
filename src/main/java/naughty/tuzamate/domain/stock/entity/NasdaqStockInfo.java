package naughty.tuzamate.domain.stock.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class NasdaqStockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "nasdaq_stock_info_seq")
    @SequenceGenerator(
            name = "nasdaq_stock_info_seq",
            sequenceName = "nasdaq_stock_info_seq",
            allocationSize = 50
    )
    private Long id;

    private String code;
    private String perx; // PER
    private String pbrx; // PBR
    private String epsx; // EPS
    private String eIcod; // 업종 섹터

    private String last; // 현재가

    private String prdtAbrvName; // 상품 약어명

}
