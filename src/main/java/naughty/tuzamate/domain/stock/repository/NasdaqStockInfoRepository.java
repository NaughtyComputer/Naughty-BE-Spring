package naughty.tuzamate.domain.stock.repository;

import naughty.tuzamate.domain.stock.entity.NasdaqStockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NasdaqStockInfoRepository extends JpaRepository<NasdaqStockInfo, Long> {
}
