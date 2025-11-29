package be.keleos.dynamicwebscraper.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class FixedPrice {

    private BigDecimal price;
    private BigDecimal fiftyFifty;
    private BigDecimal nakedPrice;

    public FixedPrice setPrice(BigDecimal price) {
        this.price = price.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        this.fiftyFifty = this.price.divide(BigDecimal.TWO, RoundingMode.HALF_UP);
        this.nakedPrice = this.price.subtract(new BigDecimal("0.17"));
        return this;
    }

}
