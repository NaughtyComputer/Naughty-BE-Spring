package naughty.tuzamate.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.global.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private boolean isRead;

    @Column(name = "target_id")
    private Long targetId; // 관련 postId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    public void updateIsRead() {
        this.isRead = !isRead;
    }
}

