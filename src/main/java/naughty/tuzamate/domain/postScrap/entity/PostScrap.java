package naughty.tuzamate.domain.postScrap.entity;

import jakarta.persistence.*;
import lombok.*;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.user.domain.User;

@Entity
@Table(name = "post_scrap")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}