package naughty.tuzamate.domain.user.entity;


import jakarta.persistence.*;
import lombok.*;
import naughty.tuzamate.global.BaseTimeEntity;
import naughty.tuzamate.domain.user.enums.SocialType;

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

    private String password;
  
    private Long gender;

    private Long age;

    @Column(name = "investment_experience")
    private boolean experience;

    private String email;

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

    private String role;

    private SocialType socialType;

   /* @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    public void updateTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }*/

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }


}
