package be.keleos.dynamicwebscraper.adaptor.octaplus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
public class OutPriceResource {

    @JsonProperty("ValidityDate")
    private String date;
    @JsonProperty("Cotations")
    private List<OutPrice> prices;

}
