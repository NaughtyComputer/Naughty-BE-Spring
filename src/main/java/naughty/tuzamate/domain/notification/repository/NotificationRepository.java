package naughty.tuzamate.domain.notification.repository;

import naughty.tuzamate.domain.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.receiver.id = :receiverId " +
            "AND (:cursor IS NULL OR n.id < :cursor) " +
            "ORDER BY n.id DESC")
    List<Notification> findByReceiverIdAndCursor(@Param("receiverId") Long receiverId,
                                                 @Param("cursor") Long cursor,
                                                 Pageable pageable);

    Optional<Notification> findByIdAndReceiverId(Long id, Long receiverId);
}
