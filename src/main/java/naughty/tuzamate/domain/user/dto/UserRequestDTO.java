package naughty.tuzamate.domain.user.dto;

import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class UserLoginDTO {
        String email;
        String password;
    }

    @Getter
    public static class UserSignUpDTO {
        String email;
        String password;
    }
}
