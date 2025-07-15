package naughty.tuzamate.domain.user.entity;


import jakarta.persistence.*;
import lombok.*;
import naughty.tuzamate.domain.user.enums.Gender;
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

    private Gender gender;

    private Long age;

    @Column(name = "investment_experience")
    private String experience;

    private String email;

    private String nickname;

    private String fundingSituation;

    private Long income;

    @Column(name = "income_stability")
    private String stability;

    @Column(name = "income_source")
    private String source;

    @Column(name = "investment_type")
    private String type;

    @Column(name = "investment_proportion")
    private String proportion;

    private Long period; // 예상 투자 기간

    private Long expectedIncome;

    private Long expectedLoss;

    @Column(name = "ivestment_purpose")
    private String purpose; // 투자 목적

    private String role;

    private SocialType socialType;

    private Long credit; // 크레딧


    /**
     * 회원의 마지막 로그아웃 이후 발급되는 토큰을 판별하기 위한 사용
     * 처음엔 0, 이후 로그아웃 시 + 1
     */
    @Column(nullable = false)
    private int tokenVersion = 0;

    public void increaseTokenVersion() {
        this.tokenVersion++;
    }

    @Column(columnDefinition = "TEXT")
    private String recentRecommendedProduct; // 최근 추천 상품

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void initProfile(String nickname, String experience) {
        this.nickname = nickname;
        this.experience = experience;
    }

}
