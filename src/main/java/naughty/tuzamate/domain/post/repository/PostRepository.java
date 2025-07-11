package naughty.tuzamate.domain.post.repository;

import naughty.tuzamate.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 커서가 없는 경우 유저가 작성한 글 최신순 조회
    @Query("select p from Post p where p.user.id = :userId and p.deletedAt is null order by p.id desc")
    Slice<Post> findPostListByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    // 커서가 있을 때 해당 커서 이전 유저가 작성한 글 최신순 조회
    @Query("select p from Post p where p.user.id = :userId and p.deletedAt is null and p.id < :cursorId order by p.id desc")
    Slice<Post> findPostListByUserIdLessThanOrderByIdDesc(Long userId, Long cursorId, Pageable pageable);
}
