package naughty.tuzamate.domain.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class StockInfoDto {

    @Getter
    @Setter
    public static class InfoDto {

        @JsonProperty("prdt_abrv_name")
        private String prdtAbrvName; // 상품 약어명
    }

}
