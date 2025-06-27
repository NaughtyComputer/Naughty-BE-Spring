package naughty.tuzamate.domain.stock.repository.code;

import naughty.tuzamate.domain.stock.entity.StockCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCodeRepository extends JpaRepository<StockCode, String> {
}
