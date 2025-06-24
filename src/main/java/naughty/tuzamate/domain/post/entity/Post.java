package naughty.tuzamate.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;
import naughty.tuzamate.domain.comment.entity.Comment;
import naughty.tuzamate.domain.postLike.entity.PostLike;
import naughty.tuzamate.global.BaseTimeEntity;
import naughty.tuzamate.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Column(name = "like_num")
    private Long likeNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void increaseLike() {
        this.likeNum = (this.likeNum == 0) ? 1 : this.likeNum + 1;
    }

    public void decreaseLike() {
        this.likeNum = (this.likeNum != null && this.likeNum > 0) ? this.likeNum - 1 : 0;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }
}
