package naughty.tuzamate.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naughty.tuzamate.auth.principal.PrincipalDetailsService;
import naughty.tuzamate.global.apiPayload.CustomResponse;
import naughty.tuzamate.global.error.BaseErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.split(" ")[1];
                String email = jwtProvider.getEmail(token);
                UserDetails userDetails = principalDetailsService.loadUserByUsername(email);

                if (userDetails != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new UserCustomException(UserErrorCode.USER_NOT_FOUND);
                }
            }

            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            BaseErrorCode code = e.getCode();
            response.setStatus(code.getStatus().value());
            response.setContentType("application/json; charset=UTF-8");

            CustomResponse<Object> customResponse = CustomResponse.onFail(code.getStatus(), code.getCode(), code.getMessage(), false, "");

            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getOutputStream(), customResponse);

        }
    }

}




