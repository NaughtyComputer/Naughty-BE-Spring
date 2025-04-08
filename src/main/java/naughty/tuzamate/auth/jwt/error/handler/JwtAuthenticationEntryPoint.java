package naughty.tuzamate.auth.jwt.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.error.GeneralErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(401);

        CustomResponse<Object> errorResponse = CustomResponse.onFail(
                GeneralErrorCode.UNAUTHORIZED_401.getStatus(),
                GeneralErrorCode.UNAUTHORIZED_401.getCode(),
                GeneralErrorCode.UNAUTHORIZED_401.getMessage(),
                false,
                null
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
