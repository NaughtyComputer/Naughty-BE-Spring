package naughty.tuzamate.domain.deposit.repository;

import naughty.tuzamate.domain.deposit.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
