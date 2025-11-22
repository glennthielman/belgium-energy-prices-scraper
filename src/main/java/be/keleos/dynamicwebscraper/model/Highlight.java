package be.keleos.dynamicwebscraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@Getter
@Setter
public class Highlight {

    private Price currentPrice;
    private Price maxPrice;
    private Price minPrice;
    private Calculations calculations;
}
