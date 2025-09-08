package naughty.tuzamate.auth.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naughty.tuzamate.auth.entity.RefreshToken;
import naughty.tuzamate.auth.repository.RefreshTokenRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void saveRefreshToken(Long userId, String token, LocalDateTime expire) {

        String key = "refreshToken:" + userId;
        Duration ttl = Duration.between(LocalDateTime.now(), expire);
        valueOperations.set(key, token, ttl);
    }

   public Optional<String> getRefreshToken(Long userId) {
       String key = "refreshToken:" + userId;
       String token = valueOperations.get(key);

       return token != null ? Optional.of(token) : Optional.empty();
   }

    public void deleteRefreshToken(Long userId) {

        String key = "refreshToken:" + userId;
        redisTemplate.delete(key);
    }
}
