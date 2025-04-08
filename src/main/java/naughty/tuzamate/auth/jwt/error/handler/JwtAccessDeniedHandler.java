package naughty.tuzamate.auth.jwt.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.error.GeneralErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    // 인가에서 예외가 터진 경우에도 저희가 한 응답통일과 같이 나가도록 직접 response에 작성한다.
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(403);

        // 반환할 응답 만들기
        CustomResponse<Object> errorResponse = CustomResponse.onFail(
                GeneralErrorCode.FORBIDDEN_403.getStatus(),
                GeneralErrorCode.FORBIDDEN_403.getCode(),
                GeneralErrorCode.FORBIDDEN_403.getMessage(),
                false,
                null
        );

        // 응답을 response에 작성
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}