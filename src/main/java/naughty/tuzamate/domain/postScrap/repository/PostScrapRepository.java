package naughty.tuzamate.domain.postScrap.repository;

import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.postScrap.entity.PostScrap;
import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {

    public boolean existsByPostAndUser(Post post, User user);
    public PostScrap findByPostAndUser(Post post, User user);

    // 커서가 없는 경우 최신순 조회
    @Query("select ps from PostScrap ps join fetch ps.post p where ps.user.id = :userId order by ps.id desc")
    Slice<PostScrap> findScrapListByUserIdOrderByIdDesc(@Param("userId") Long userId, Pageable pageable);


    // 커서 있을 때 해당 커서 이전 데이터 최신순 조회
    @Query("select ps from PostScrap ps join fetch ps.post p where ps.user.id = :userId and ps.id < :cursorId order by ps.id desc")
    Slice<PostScrap> findScrapListByUserIdLessThanOrderByIdDesc(@Param("userId") Long userId, @Param("cursorId") Long cursorId, Pageable pageable);

}
