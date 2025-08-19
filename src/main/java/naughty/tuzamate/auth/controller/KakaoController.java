package naughty.tuzamate.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.dto.kakao.KakaoLoginRequest;
import naughty.tuzamate.auth.dto.kakao.KakaoOAuth2DTO;
import naughty.tuzamate.auth.service.OAuth2Service;
import naughty.tuzamate.auth.service.RefreshTokenService;
import naughty.tuzamate.auth.success.AuthSuccessCode;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.domain.user.enums.SocialType;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "카카오 로그인", description = "카카오 소셜 로그인 관련")
public class KakaoController {

    private final OAuth2Service oAuth2Service;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/oauth2/kakao-login")
    @Operation(summary = "카카오 로그인 백엔드 테스트용", description = "카카오 소셜 로그인 인가 코드 발급을 수행")
    public CustomResponse<?> login() {

        String code = oAuth2Service.getCode();
        return CustomResponse.onSuccess(AuthSuccessCode.AUTH_SUCCESS_CODE, code);
    }


    @GetMapping("/auth/kakao-oauth")
    public CustomResponse<?> KakaoLogin(@RequestParam("code") String code) {

        UserResponseDTO.UserTokenDTO kakaoToken = oAuth2Service.login("kakao", code);
        refreshTokenService.saveRefreshToken(
                kakaoToken.getUserId(),
                kakaoToken.getRefreshToken(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(kakaoToken.getRefreshTokenExpire().getTime()), ZoneId.of("Asia/Seoul")));


        return CustomResponse.onSuccess(kakaoToken);
    }

    @PostMapping("/auth/kakao-login")
    @Operation(summary = "카카오 로그인 (안드로이드 SDK 방식)", description = "안드로이드에서 전달된 카카오 액세스 토큰을 검증 및 사용자 정보 처리")
    public CustomResponse<?> kakaoLogin(@RequestBody @Valid KakaoLoginRequest request) {

        // 카카오 액세스 토큰 검증, 사용자 정보 가져오기
        KakaoOAuth2DTO.KakaoProfile profileFromKakao = oAuth2Service.getProfileFromKakao(request.accessToken());

        // 사용자 이메일, 카카오 타입으로 로그인 및 회원가입 처리
        String email = profileFromKakao.getKakao_account().getEmail();
        UserResponseDTO.UserTokenDTO userTokenDTO = oAuth2Service.loginAndSignUp(SocialType.KAKAO, email);

        refreshTokenService.saveRefreshToken(
                userTokenDTO.getUserId(),
                userTokenDTO.getRefreshToken(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(userTokenDTO.getRefreshTokenExpire().getTime()), ZoneId.of("Asia/Seoul")));


        return CustomResponse.onSuccess(userTokenDTO);
    }
}
