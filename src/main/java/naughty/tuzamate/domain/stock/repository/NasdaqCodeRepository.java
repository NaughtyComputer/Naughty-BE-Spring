package naughty.tuzamate.domain.stock.repository;

import naughty.tuzamate.domain.stock.entity.NasdaqStockCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NasdaqCodeRepository extends JpaRepository<NasdaqStockCode, Long> {
}
