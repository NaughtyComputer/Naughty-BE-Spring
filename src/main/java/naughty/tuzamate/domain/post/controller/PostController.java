package naughty.tuzamate.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.post.dto.PostReqDTO;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.enums.BoardType;
import naughty.tuzamate.domain.post.service.command.PostCommandService;
import naughty.tuzamate.domain.post.service.query.PostQueryService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시판 컨트롤러", description = "게시판 관련 API")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @PostMapping("/boards/{boardType}/posts")
    @Operation(summary = "게시글 생성", description = "게시판에 맞는 게시글을 생성합니다.")
    public CustomResponse<PostResDTO.CreatePostResponseDTO> createPost(
            @PathVariable BoardType boardType,
            @RequestBody PostReqDTO.CreatePostRequestDTO reqDTO,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        PostResDTO.CreatePostResponseDTO resDTO = postCommandService.createPost(boardType, reqDTO, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, resDTO);
    }

    @GetMapping("/boards/{boardType}/posts/{postId}")
    @Operation(summary = "단일 게시글 조회", description = "단일 게시글을 조회합니다.")
    public CustomResponse<PostResDTO.PostDTO> getPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        PostResDTO.PostDTO resDTO = postQueryService.getPost(postId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @GetMapping("/boards/{boardType}/posts")
    @Operation(summary = "게시글 목록 조회", description = "각 BoardType(FREE, INFO)에 맞는 게시글 목록을 조회합니다.")
    public CustomResponse<PostResDTO.PostPreviewListDTO> getPostList(
            @PathVariable BoardType boardType,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Max(10) int size) {
        PostResDTO.PostPreviewListDTO resDTO = postQueryService.getPostList(boardType, cursor, size);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @PatchMapping("/boards/{boardType}/posts/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    public CustomResponse<PostResDTO.UpdatePostResponseDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostReqDTO.UpdatePostRequestDTO reqDTO,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        PostResDTO.UpdatePostResponseDTO resDTO = postCommandService.updatePost(reqDTO, postId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @DeleteMapping("/boards/{boardType}/posts/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    public CustomResponse<PostResDTO.DeletePostResponseDTO> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        PostResDTO.DeletePostResponseDTO resDTO = postCommandService.deletePost(postId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @PostMapping("posts/{postId}/likes")
    @Operation(summary = "게시글 좋아요 증가", description = "게시글에 좋아요를 눌러 PostLike에 PostId를 저장하고, Post의 likeNum을 증가시킵니다.")
    public CustomResponse<String> increaseLike(
            @PathVariable Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        String message = postCommandService.postLike(postId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, message);
    }

    @DeleteMapping("posts/{postId}/likes")
    @Operation(summary = "게시글 좋아요 감소", description = "게시글에 좋아요를 다시 눌러 PostLike를 삭제하고, Post의 likeNum을 감소시킵니다.")
    public CustomResponse<String> deleteLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        String message = postCommandService.deleteLike(postId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, message);
    }

    @PostMapping("posts/{postId}/scraps")
    @Operation(summary = "게시글 스크랩 기능", description = "스크랩 기능을 이용해 PostScrap에 저장합니다.")
    public CustomResponse<String> postScrap(
            @PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        String message = postCommandService.postScrap(postId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, message);
    }

    @DeleteMapping("posts/{postId}/scraps")
    @Operation(summary = "게시글 스크랩 취소 기능", description = "스크랩 기능을 재이용해 PostScrap을 삭제합니다.")
    public CustomResponse<String> deleteScrap(
            @PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        String message = postCommandService.deleteScrap(postId, principalDetails);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, message);
    }
}
