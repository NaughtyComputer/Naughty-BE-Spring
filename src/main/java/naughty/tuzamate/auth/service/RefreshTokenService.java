package naughty.tuzamate.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naughty.tuzamate.auth.entity.RefreshToken;
import naughty.tuzamate.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(Long userId, String token, LocalDateTime expire) {

        deleteToken(userId);

        RefreshToken refreshToken = new RefreshToken(token, userId, expire);
        refreshTokenRepository.save(refreshToken);
    }

    public boolean validateToken(String token) {

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken.isPresent() && refreshToken.get().getExpireDate().isAfter(LocalDateTime.now());
    }

    public void deleteToken(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
