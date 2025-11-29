package be.keleos.dynamicwebscraper.adaptor.eneco.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class OutEnecoPrices {

    private OutData data;
}
