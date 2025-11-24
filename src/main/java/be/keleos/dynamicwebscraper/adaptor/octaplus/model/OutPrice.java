package be.keleos.dynamicwebscraper.adaptor.octaplus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class OutPrice {

    @JsonProperty("TimeRange")
    private String time;
    @JsonProperty("PriceAmount")
    private String priceInMwH;

}
