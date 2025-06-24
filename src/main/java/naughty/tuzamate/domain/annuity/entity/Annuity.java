package naughty.tuzamate.domain.annuity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "annuity")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Annuity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String korCoNm; // 운용사명

    private String finPrdtNm; // 금융 상품명

    private String pnsnKindNm; // 연금 종류 (예: 연금저축펀드)

    private String prdtTypeNm; // 상품유형명 (예: 주식형)

    private Double avgPrftRate; // 평균 수익률

    private Double guarRate; // 보장 수익률

    private Double btrmPrftRate1; // 최고 수익률

    private String joinWay; // 가입 방법

    @Column(columnDefinition = "TEXT")
    private String saleCo; // 판매사 목록

    private String saleStrtDay; // 판매 시작일

}
