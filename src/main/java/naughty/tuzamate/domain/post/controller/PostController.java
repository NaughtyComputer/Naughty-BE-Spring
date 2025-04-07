package naughty.tuzamate.domain.post.controller;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.post.dto.PostReqDTO;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.service.command.PostCommandService;
import naughty.tuzamate.domain.post.service.query.PostQueryService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    // successCode 수정할 것

    @PostMapping("")
    public CustomResponse<PostResDTO.CreatePostResponseDTO> createPost(
            @RequestBody PostReqDTO.CreatePostRequestDTO reqDTO) {
        PostResDTO.CreatePostResponseDTO resDTO = postCommandService.createPost(reqDTO);

        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, resDTO);
    }

    @GetMapping("/{postId}")
    public CustomResponse<PostResDTO.PostPreviewDTO> getPost(@PathVariable Long postId) {
        PostResDTO.PostPreviewDTO resDTO = postQueryService.getPost(postId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @GetMapping("")
    public CustomResponse<PostResDTO.PostPreviewListDTO> getPostList() {
        PostResDTO.PostPreviewListDTO resDTO = postQueryService.getPostList();

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @PatchMapping("/{postId}")
    public CustomResponse<PostResDTO.UpdatePostResponseDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostReqDTO.UpdatePostRequestDTO reqDTO) {
        PostResDTO.UpdatePostResponseDTO resDTO = postCommandService.updatePost(reqDTO, postId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }

    @DeleteMapping("/{postId}")
    public CustomResponse<PostResDTO.DeletePostResponseDTO> deletePost(@PathVariable Long postId) {
        PostResDTO.DeletePostResponseDTO resDTO = postCommandService.deletePost(postId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, resDTO);
    }
}
