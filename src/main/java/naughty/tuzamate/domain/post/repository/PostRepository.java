package naughty.tuzamate.domain.post.repository;

import naughty.tuzamate.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
