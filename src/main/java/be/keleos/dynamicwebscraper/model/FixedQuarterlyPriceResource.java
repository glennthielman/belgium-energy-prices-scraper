package be.keleos.dynamicwebscraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
public class FixedQuarterlyPriceResource {

    List<FixedQuarterlyPrice> prices = new ArrayList<>();

}
