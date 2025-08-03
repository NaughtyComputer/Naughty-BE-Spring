package naughty.tuzamate.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.auth.service.RefreshTokenService;
import naughty.tuzamate.domain.user.dto.FcmRequestDTO;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.domain.user.dto.UserRequestDTO;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.domain.user.service.UserService;
import naughty.tuzamate.global.success.GeneralSuccessCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
@Tag(name = "일반 로그인 및 회원가입", description = "소셜 로그인이 아닌 일반 로그인으로 로그인 및 가입")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public CustomResponse<?> login(@RequestBody UserRequestDTO.UserLoginDTO loginDTO) {

        // RefreshToken을 저장하고, 그에 따른 로직을 수행한 후 로그인 결과 반환

        UserResponseDTO.UserTokenDTO loginResult = userService.login(loginDTO);
        refreshTokenService.saveRefreshToken(loginResult.getUserId(), loginResult.getRefreshToken(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(loginResult.getRefreshTokenExpire().getTime()), ZoneId.of("Asia/Seoul")));

        return CustomResponse.onSuccess(loginResult);
    }

    @PostMapping("/signUp")
    public CustomResponse<?> singUp(@RequestBody UserRequestDTO.UserSignUpDTO signUpDTO) {

        UserResponseDTO.UserTokenDTO signUpResult = userService.signUp(signUpDTO);
        return CustomResponse.onSuccess(signUpResult);
    }

    @PostMapping("/users/fcm-token")
    @Operation(summary = "fcm 토큰 발급받아 저장하는 api",
            description = "프론트에서 로그인 및 회원가입시 FCM 토큰을 발행하여 이 api로 토큰을 전송하면 DB에 저장 및 업데이트")
    public CustomResponse<?> saveFcmToken(@AuthenticationPrincipal PrincipalDetails principal,
                                          @RequestBody FcmRequestDTO token) {
        userService.updateFcmToken(principal.getUser().getId(), token.getToken());

        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }
}
