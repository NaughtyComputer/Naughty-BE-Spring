package naughty.tuzamate.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.comment.dto.CommentReqDTO;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;
import naughty.tuzamate.domain.comment.service.command.CommentCommandService;
import naughty.tuzamate.domain.comment.service.query.CommentQueryService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name = "댓글 컨트롤러", description = "댓글 관련 API")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("{postId}/comments")
    @Operation(summary = "댓글 및 대댓글 생성",
            description = "게시글에 댓글을 달거나, 현재 존재하는 댓글 밑에 대댓글을 다는 기능을 수행합니다.")
    public CustomResponse<CommentResDTO.CreateCommentResponseDTO> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody CommentReqDTO.CreateCommentRequestDTO reqDTO
    ) {
        CommentResDTO.CreateCommentResponseDTO resDTO = commentCommandService
                .createComment(reqDTO, postId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, resDTO);
    }

    @GetMapping("{postId}/comments/{commentId}")
    @Operation(summary = "댓글 및 대댓글 단일 조회",
            description = "현재 존재하는 댓글 및 대댓글을 조회하는 기능을 수행합니다.")
    public CustomResponse<CommentResDTO.CommentPreviewDTO> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        CommentResDTO.CommentPreviewDTO resDTO = commentQueryService.getComment(commentId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @GetMapping("{postId}/comments")
    @Operation(summary = "댓글 및 대댓글 목록 조회",
            description = "게시글을 선택했을 때 현재 존재하는 댓글 및 대댓글 목록을 조회하는 기능을 수행합니다.")
    public CustomResponse<CommentResDTO.CommentPreviewListDTO> getCommentList(
            @PathVariable Long postId,
             @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        CommentResDTO.CommentPreviewListDTO resDTO = commentQueryService.getCommentList(postId, cursor, size);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @PatchMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "댓글 및 대댓글 수정",
            description = "현재 존재하는 댓글 및 대댓글을 수정하는 기능을 수행합니다.")
    public CustomResponse<CommentResDTO.UpdateCommentResponseDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentReqDTO.UpdateCommentRequestDTO reqDTO,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        CommentResDTO.UpdateCommentResponseDTO resDTO = commentCommandService.updateComment(reqDTO, commentId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "댓글 및 대댓글 삭제",
            description = "현재 존재하는 댓글 및 대댓글을 삭제하는 기능을 수행합니다.")
    public CustomResponse<CommentResDTO.DeleteCommentResponseDTO> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        CommentResDTO.DeleteCommentResponseDTO resDTO = commentCommandService.deleteComment(commentId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }
}
