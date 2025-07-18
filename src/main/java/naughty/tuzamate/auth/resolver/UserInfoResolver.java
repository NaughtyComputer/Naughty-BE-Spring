package naughty.tuzamate.auth.resolver;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.auth.annotation.UserInfo;
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

        // 인증 정보가 없거나 PrincipalDetails가 아닌 경우 예외 처리
        if (authentication == null || !(authentication.getPrincipal() instanceof PrincipalDetails)) {
            throw new UserCustomException(UserErrorCode.UNAUTHORIZED_USER);
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return principalDetails.getUser();

    }
}
