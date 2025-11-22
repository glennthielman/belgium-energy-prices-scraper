package be.keleos.dynamicwebscraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Getter
@Setter
public class Price {

    private static final BigDecimal DIVIDE_TO_KWH = new BigDecimal(1000);

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal priceMwH;
    private BigDecimal priceKwH;

    public Price setPrice(BigDecimal price) {
        this.priceMwH = price;
        this.priceKwH = price.divide(DIVIDE_TO_KWH);
        return this;
    }

}
