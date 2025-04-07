package naughty.tuzamate.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.comment.dto.CommentReqDTO;
import naughty.tuzamate.domain.comment.dto.CommentResDTO;
import naughty.tuzamate.domain.comment.service.command.CommentCommandService;
import naughty.tuzamate.domain.comment.service.query.CommentQueryService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("{postId}/comments")
    public CustomResponse<CommentResDTO.CreateCommentResponseDTO> createComment(
            @RequestBody CommentReqDTO.CreateCommentRequestDTO reqDTO) {
        CommentResDTO.CreateCommentResponseDTO resDTO = commentCommandService.createComment(reqDTO);

        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, resDTO);
    }

    @GetMapping("{postId}/comments/{commentId}")
    public CustomResponse<CommentResDTO.CommentPreviewDTO> getComment(
            @PathVariable Long commentId) {
        CommentResDTO.CommentPreviewDTO resDTO = commentQueryService.getComment(commentId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @GetMapping("{postId}/comments")
    public CustomResponse<CommentResDTO.CommentPreviewListDTO> getCommentList(@PathVariable Long postId) {
        CommentResDTO.CommentPreviewListDTO resDTO = commentQueryService.getCommentList(postId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @PatchMapping("/{postId}/comments/{commentId}")
    public CustomResponse<CommentResDTO.UpdateCommentResponseDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentReqDTO.UpdateCommentRequestDTO reqDTO) {
        CommentResDTO.UpdateCommentResponseDTO resDTO = commentCommandService.updateComment(reqDTO, commentId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public CustomResponse<CommentResDTO.DeleteCommentResponseDTO> deleteComment(@PathVariable Long commentId) {
        CommentResDTO.DeleteCommentResponseDTO resDTO = commentCommandService.deleteComment(commentId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }
}
