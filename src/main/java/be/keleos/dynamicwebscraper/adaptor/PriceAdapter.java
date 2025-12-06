package be.keleos.dynamicwebscraper.adaptor;

import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceProvider;

public interface PriceAdapter {
    PriceResource getPrices();

    PriceProvider getProvider();
}
