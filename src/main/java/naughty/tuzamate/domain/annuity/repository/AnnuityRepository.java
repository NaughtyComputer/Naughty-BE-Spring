package naughty.tuzamate.domain.annuity.repository;

import naughty.tuzamate.domain.annuity.entity.Annuity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnuityRepository extends JpaRepository<Annuity, Long> {
}
