package naughty.tuzamate.global.config;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.resolver.UserIdInfoResolver;
import naughty.tuzamate.auth.resolver.UserInfoResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserInfoResolver userInfoResolver;
    private final UserIdInfoResolver userIdInfoResolver;

    @Bean

    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userInfoResolver);
        resolvers.add(userIdInfoResolver);
    }
}
