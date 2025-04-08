package naughty.tuzamate.domain.postScrap.repository;

import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.postScrap.entity.PostScrap;
import naughty.tuzamate.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {

    public boolean existsByPostAndUser(Post post, User user);
    public PostScrap findByPostAndUser(Post post, User user);

}
