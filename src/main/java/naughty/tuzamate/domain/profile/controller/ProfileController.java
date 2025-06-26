package naughty.tuzamate.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
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
//        if (!getUser.getEmail().equals(userDetails.getUsername())) {
//            // 현재 로그인한 사용자의 이메일과 조회하려는 사용자의 이메일이 다를 경우
//            return CustomResponse.onFail(UserErrorCode.UNAUTHORIZED_USER);
//        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, ProfileConverter.from(getUser));
        
    }
}
