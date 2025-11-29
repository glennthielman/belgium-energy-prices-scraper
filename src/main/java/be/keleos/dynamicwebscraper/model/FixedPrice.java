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
        this.price = price;
        this.fiftyFifty = price.divide(BigDecimal.TWO, RoundingMode.DOWN);
        this.nakedPrice = price.subtract(new BigDecimal(17));
        return this;
    }

}
