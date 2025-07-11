package naughty.tuzamate.domain.post.repository;

import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.enums.BoardType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
    SELECT p FROM Post p
    WHERE p.boardType = :boardType
      AND (:cursor IS NULL OR p.id < :cursor)
    ORDER BY p.id DESC
    """)
    Slice<Post> findByBoardTypeAndCursor(BoardType boardType, Long cursor, Pageable pageable);
}
