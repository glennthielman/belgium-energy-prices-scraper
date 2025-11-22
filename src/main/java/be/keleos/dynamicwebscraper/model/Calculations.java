package be.keleos.dynamicwebscraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@Getter
@Setter
public class Calculations {

    private BigDecimal averagePriceKwH;
    private BigDecimal averagePriceMwH;

}
