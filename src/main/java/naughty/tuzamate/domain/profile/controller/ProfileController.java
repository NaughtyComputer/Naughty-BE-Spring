package naughty.tuzamate.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.profile.converter.ProfileConverter;
import naughty.tuzamate.domain.profile.dto.request.ProfileRequestDTO;
import naughty.tuzamate.domain.profile.dto.response.ProfileResponseDTO;
import naughty.tuzamate.domain.profile.service.command.ProfileCommandService;
import naughty.tuzamate.domain.profile.service.query.ProfileQueryService;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.success.GeneralSuccessCode;
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

    @GetMapping("/{userId}")
    @Operation(summary = "프로필 조회")
    public CustomResponse<?> getProfile(@PathVariable("userId") Long userId) {

        User getUser = profileQueryService.getProfile(userId);
        
        // 추후에 UserResponseDTO.UserProfileDTO.from 을 ProfileResponseDTO와 합친 후 ProfileConverter를 이용해 반환할 것
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.UserProfileDTO.from(getUser));
        
    }
}
