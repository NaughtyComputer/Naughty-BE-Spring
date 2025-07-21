package naughty.tuzamate.domain.comment.repository;

import naughty.tuzamate.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public List<Comment> findByPostId(Long postId);

    // parent 댓글들 목록 조회
    @Query("""
        SELECT c
        FROM Comment c
        WHERE c.post.id = :postId
          AND c.parent IS NULL
          AND (:cursor IS NULL OR c.id > :cursor)
        ORDER BY c.id ASC
    """)
    Slice<Comment> findRootCommentsCursorAsc(@Param("postId") Long postId, @Param("cursor") Long cursor, Pageable pageable);

    // parent 댓글 id를 기준으로 child 댓글들 조회
    @Query("""
        SELECT c
        FROM Comment c
        WHERE c.parent.id IN :parentId
        ORDER BY c.id ASC
    """)
    List<Comment> findChildrenInParentIds(@Param("parentId") List<Long> parentId);
}
