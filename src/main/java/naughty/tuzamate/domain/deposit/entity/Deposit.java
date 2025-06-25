package naughty.tuzamate.domain.deposit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deposit")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String korCoNm;       // 금융 회사명

    private String finPrdtNm;     // 상품명

    private String joinWay;       // 가입 방법

    private String joinMember;    // 가입 대상

    @Column(length = 1000)
    private String mtrtInt;       // 만기 후 이자율

    @Column(length = 1000)
    private String spclCnd;       // 우대 조건

    @Column(length = 1000)
    private String etcNote;       // 기타 유의사항
}
