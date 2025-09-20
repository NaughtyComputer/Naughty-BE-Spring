package naughty.tuzamate.domain.pushToken.entity;

import jakarta.persistence.*;
import lombok.*;
import naughty.tuzamate.domain.pushToken.enums.Platform;
import naughty.tuzamate.global.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "push_token",
        uniqueConstraints = @UniqueConstraint(name = "uk_push_token_token", columnNames = "token"),
        indexes = {
                // 한 유저가 여러 기기 사용하는 경우 빠르게 조회 가능
                @Index(name = "idx_push_token_user_id", columnList = "userId"),
                @Index(name = "idx_push_token_is_active", columnList = "isActive")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Long userId;

    @Column(nullable = false, length = 2048)
    private String token;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(length = 256)
    private String deviceId;

    // logout 시 false -> 재 로그인 시 true 재 변경
    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = false;

    // 앱을 실행할 때마다 업데이트가 되게 하기(프론트에서 해야 할 듯)
    private LocalDateTime lastSeenAt;

    @PrePersist
    void prePersist() {
        if (lastSeenAt == null) lastSeenAt = LocalDateTime.now();
    }

}
