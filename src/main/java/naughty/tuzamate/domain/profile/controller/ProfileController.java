package naughty.tuzamate.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.profile.converter.ProfileConverter;
import naughty.tuzamate.domain.profile.dto.request.ProfileRequestDTO;
import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.profile.service.command.ProfileCommandService;
import naughty.tuzamate.domain.profile.service.query.ProfileQueryService;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.global.annotation.UserInfo;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "프로필 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    @PutMapping("/{userId}")
    @Operation(summary = "프로필 수정")
    public CustomResponse<?> updateProfile(@PathVariable("userId") Long userId,
                                           @RequestBody ProfileRequestDTO requestDTO) {

        User updateUser = profileCommandService.updateProfile(userId, requestDTO);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, ProfileConverter.toProfileResponseDTO(updateUser));
    }

    @GetMapping("")
    @Operation(summary = "프로필 조회")
    public CustomResponse<?> getProfile(@UserInfo User user) {

        User getUser = profileQueryService.getProfile(user.getId());

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, ProfileConverter.from(getUser));

    }

    @GetMapping("/scraps")
    @Operation(summary = "스크랩한 게시글 조회")
    @Parameters({
            @Parameter(name = "cursor", description = "스크랩 목록의 커서 값. 처음 조회 시 0을 입력", required = false, example = "0"),
            @Parameter(name = "offset", description = "한 번에 가져올 스크랩 개수. 기본 값은 10", required = false, example = "10")
    })
    public CustomResponse<?> getScrapList(
            @UserInfo User user,
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "offset", defaultValue = "10") int offset) {

        ProfileResponseDTO.profileCommunityListResponse scrapList = profileQueryService.getScrapList(user, cursor, offset);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, scrapList);

    }

    @GetMapping("/likes")
    @Operation(summary = "좋아요한 게시글 조회")
    @Parameters({
            @Parameter(name = "cursor", description = "좋아요 목록의 커서 값. 처음 조회 시 0을 입력", required = false, example = "0"),
            @Parameter(name = "offset", description = "한 번에 가져올 좋아요 개수. 기본 값은 10", required = false, example = "10")
    })
    public CustomResponse<?> getLikeList(
            @UserInfo User user,
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "offset", defaultValue = "10") int offset) {

        ProfileResponseDTO.profileCommunityListResponse likeList = profileQueryService.getLikeList(user, cursor, offset);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, likeList);
    }

    @GetMapping("/posts")
    @Operation(summary = "작성한 게시글 조회")
    @Parameters({
            @Parameter(name = "cursor", description = "게시글 목록의 커서 값. 처음 조회 시 0을 입력", required = false, example = "0"),
            @Parameter(name = "offset", description = "한 번에 가져올 게시글 개수. 기본 값은 10", required = false, example = "10")
    })
    public CustomResponse<?> getPostList(
            @UserInfo User user,
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "offset", defaultValue = "10") int offset) {

        ProfileResponseDTO.profileCommunityListResponse postList = profileQueryService.getPostList(user, cursor, offset);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, postList);
    }
}
