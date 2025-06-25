package naughty.tuzamate.domain.savings.repository;

import naughty.tuzamate.domain.savings.entity.Savings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepository extends JpaRepository<Savings, Long> {
}
