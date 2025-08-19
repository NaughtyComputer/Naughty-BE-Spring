package naughty.tuzamate.auth.service;

import naughty.tuzamate.auth.dto.kakao.KakaoOAuth2DTO;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;
import naughty.tuzamate.domain.user.enums.SocialType;

import java.util.Map;

public interface OAuth2Service {

    UserResponseDTO.UserTokenDTO login(String provider, String code);

    String getCode();

    KakaoOAuth2DTO.KakaoProfile getProfileFromKakao(String accessToken);

    public UserResponseDTO.UserTokenDTO loginAndSignUp(SocialType socialType, String email);
}
