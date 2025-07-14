package naughty.tuzamate.auth.resolver;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.annotation.UserIdInfo;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.exception.CustomException;
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
public class UserIdInfoResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(UserIdInfo.class) &&
                parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserCustomException(UserErrorCode.UNAUTHORIZED_USER);
        }

        /**
         * jwt Filter 에서 jwt 추출
         * token 검증
         * 검증 후, Authentication 객체 생성
         * SecurityContext에 Authentication 객체 저장
         */

        // principal은 PrincipalDetails 객체임
        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            // PrincipalDetails로 타입 캐스팅
            PrincipalDetails principalDetails = (PrincipalDetails) principal;

            return principalDetails.getId();
        }

        // anonymousUser는 Spring Security에서 인증되지 않은 사용자를 나타내는 기본 문자열 이라고 한다.
        if (principal == null || "anonymousUser".equals(principal.toString())) {
            throw new UserCustomException(UserErrorCode.UNAUTHORIZED_USER);
        }

        // principal이 UserDetails가 아닌 경우
        throw new UserCustomException(UserErrorCode.INVALID_USER_ID_FORMAT);



    }
}
