package naughty.tuzamate.domain.pushToken.repository;

import naughty.tuzamate.domain.pushToken.entity.PushToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PushTokenRepository extends JpaRepository<PushToken, String> {
    Optional<PushToken> findByToken(String token);
    List<PushToken> findAllByUserIdAndIsActiveTrue(Long userId);
}
