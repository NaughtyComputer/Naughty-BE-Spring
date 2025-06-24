package naughty.tuzamate.global.resolver;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.jwt.error.JwtErrorCode;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.annotation.UserInfo;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserInfoResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserInfo.class) &&
                parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));
            }

        throw new UserCustomException(UserErrorCode.UNAUTHORIZED_USER);
    }
}
