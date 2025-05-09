package naughty.tuzamate.domain.profile.repository;

import naughty.tuzamate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<User, Long> {
}
