package naughty.tuzamate.domain.pushToken.repository;

import naughty.tuzamate.domain.pushToken.entity.PushToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PushTokenRepository extends JpaRepository<PushToken, Long> {
    Optional<PushToken> findByToken(String token);
    List<PushToken> findAllByUserIdAndIsActiveTrue(Long userId);

    @Query("select pt.token from PushToken pt where pt.userId = :userId and pt.isActive = true")
    List<String> findActiveTokensByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update PushToken pt set pt.isActive=false where pt.token in :tokens")
    void bulkDeactivateByTokens(@Param("tokens") List<String> tokens);
}
