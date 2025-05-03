package naughty.tuzamate.domain.postLike.repository;

import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.postLike.entity.PostLike;
import naughty.tuzamate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    public boolean existsByPostAndUser(Post post, User user);
    public PostLike findByPostAndUser(Post post, User user);

}
