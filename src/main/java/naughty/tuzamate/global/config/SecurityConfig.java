package naughty.tuzamate.global.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
//import naughty.tuzamate.auth.jwt.JwtFilter;
//import naughty.tuzamate.auth.jwt.JwtProvider;
//import naughty.tuzamate.auth.jwt.LoginFilter;
import naughty.tuzamate.auth.jwt.JwtFilter;
import naughty.tuzamate.auth.jwt.JwtProvider;
import naughty.tuzamate.auth.jwt.error.handler.JwtAccessDeniedHandler;
import naughty.tuzamate.auth.jwt.error.handler.JwtAuthenticationEntryPoint;
import naughty.tuzamate.auth.principal.PrincipalDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final PrincipalDetailsService principalDetailsService;
    private final JwtProvider jwtProvider;

    private final String[] allowUrls = {
            "/",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/auth/kakao-oauth",
            "/signUp",
            "/login"
    };

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public Filter jwtFilter() {
        return new JwtFilter(jwtProvider, principalDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .cors((cors) -> cors.configurationSource(corsConfig.configurationSource()))
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                // oauth2
                .oauth2Login(Customizer.withDefaults())
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(allowUrls).permitAll()
                        .anyRequest().authenticated())

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 인가 예외
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        // 인증 예외
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
