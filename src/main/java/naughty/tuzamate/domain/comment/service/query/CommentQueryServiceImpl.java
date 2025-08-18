package naughty.tuzamate.domain.comment.service.query;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.comment.code.CommentErrorCode;
import naughty.tuzamate.domain.comment.converter.CommentConverter;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;
import naughty.tuzamate.domain.comment.entity.Comment;
import naughty.tuzamate.domain.comment.repository.CommentRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentRepository commentRepository;

    @Override
    public CommentResDTO.CommentPreviewDTO getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        return CommentConverter.toCommentPreviewDTO(comment, List.of());
    }

    @Override
    public CommentResDTO.CommentPreviewListDTO getCommentList(Long postId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<Comment> rootSlice = commentRepository.findRootCommentsCursorAsc(postId, cursor, pageable);

        List<Comment> roots = rootSlice.getContent();

        List<Long> parentIds = roots.stream()
                .map(Comment::getId)
                .toList();

        Map<Long, List<Comment>> childMap = commentRepository. findChildrenInParentIds(parentIds)
                .stream()
                .collect(Collectors.groupingBy(c -> c.getParent().getId()));

        List<CommentResDTO.CommentPreviewDTO> dtoList = roots.stream()
                .map(root -> CommentConverter.toCommentPreviewDTO(root, childMap.getOrDefault(root.getId(),List.of()))).toList();

        Long nextCursor = rootSlice.hasNext()
                ? dtoList.get(dtoList.size() - 1).id()
                : null;

        return CommentConverter.toCommentPreviewListDTO(dtoList,
                rootSlice.hasNext(),
                nextCursor);
    }
}
