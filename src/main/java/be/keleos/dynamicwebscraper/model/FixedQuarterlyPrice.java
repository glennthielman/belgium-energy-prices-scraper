package be.keleos.dynamicwebscraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class FixedQuarterlyPrice {

    private int year;
    private int quarter;
    private FixedPrice priceFlanders;
    private FixedPrice priceBrussels;
    private FixedPrice priceWallonia;
}
