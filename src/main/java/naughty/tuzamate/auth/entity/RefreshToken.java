package naughty.tuzamate.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RefreshToken {

    @Id
    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expireDate;

    public RefreshToken(String token, Long userId, LocalDateTime expireDate) {
        this.token = token;
        this.userId = userId;
        this.expireDate = expireDate;
    }

    public void update(String token) {
        this.token = token;
    }

}
