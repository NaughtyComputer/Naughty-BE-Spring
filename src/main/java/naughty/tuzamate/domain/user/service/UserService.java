package naughty.tuzamate.domain.user.service;

import naughty.tuzamate.domain.user.dto.UserRequestDTO;
import naughty.tuzamate.domain.user.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO.UserTokenDTO login(UserRequestDTO.UserLoginDTO loginDTO);

    UserResponseDTO.UserTokenDTO signUp(UserRequestDTO.UserSignUpDTO signUpDTO);


}
