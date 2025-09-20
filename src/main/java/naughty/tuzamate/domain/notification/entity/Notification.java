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
@Table(indexes = {
        @Index(name="idx_notification_receiver_id_id_desc", columnList = "receiver_id,id")
})
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Builder.Default
    private boolean isRead = false;;

    @Column(name = "target_id")
    private Long targetId; // 관련 postId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    public void markAsRead()   { this.isRead = true; }

    public void markAsUnread() { this.isRead = false; }
}

