package naughty.tuzamate.domain.postLike.repository;

import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.postLike.entity.PostLike;
import naughty.tuzamate.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    public boolean existsByPostAndUser(Post post, User user);
    public PostLike findByPostAndUser(Post post, User user);
    public boolean existsByPostIdAndUserId(Long postId, Long userId);

    // 커서가 없는 경우 최신순 조회
    @Query("select pl from PostLike pl join fetch pl.post p where pl.user.id = :userId order by pl.id desc")
    Slice<PostLike> findLikeListByUserIdOrderByIdDesc(@Param("userId") Long userId, Pageable pageable);

    // 커서 있을 때 해당 커서 이전 데이터 최신순 조회
    @Query("select pl from PostLike pl join fetch pl.post p where pl.user.id = :userId and pl.id < :cursorId order by pl.id desc")
    Slice<PostLike> findLikeListByUserIdLessThanOrderByIdDesc(@Param("userId") Long userId, @Param("cursorId") Long cursorId, Pageable pageable);
}
