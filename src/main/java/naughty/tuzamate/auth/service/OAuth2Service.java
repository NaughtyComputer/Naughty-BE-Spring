package naughty.tuzamate.auth.service;

import naughty.tuzamate.domain.user.dto.UserResponseDTO;

public interface OAuth2Service {

    UserResponseDTO.UserTokenDTO login(String provider, String code);
}
