package naughty.tuzamate.user.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naughty.tuzamate.global.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long gender;

    private Long age;

    @Column(name = "investment_experience")
    private boolean experience;

    private String nickname;

    private String funding_situation;

    private Long income;

    @Column(name = "income_stability")
    private String stability;

    @Column(name = "income_source")
    private String source;

    @Column(name = "investment_type")
    private String type;

    @Column(name = "investment_proportion")
    private String proportion;

    private Long period;

    private Long expected_income;

    private Long expected_loss;

    private String purpose;


}
