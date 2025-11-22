package be.keleos.dynamicwebscraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
public class PriceResource {

    private List<Price> prices = new ArrayList<>();
    private List<Price> pricesDayAhead = new ArrayList<>();

}
