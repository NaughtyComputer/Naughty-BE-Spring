package naughty.tuzamate.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import naughty.tuzamate.global.BaseTimeEntity;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 대댓글 기능 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 댓글이 삭제되면 대댓글도 같이 자동으로 삭제 설정 및 부모 변경이 자식에게 전파됨
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public void updateContent(String content) {
        this.content = content;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
