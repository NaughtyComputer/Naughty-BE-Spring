package naughty.tuzamate.domain.stock.repository;

import naughty.tuzamate.domain.stock.entity.KrxStockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KrxStockInfoRepository  extends JpaRepository<KrxStockInfo, Long> {
}
