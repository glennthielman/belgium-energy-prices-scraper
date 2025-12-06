package be.keleos.dynamicwebscraper.adaptor;

import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceProvider;
import org.springframework.stereotype.Service;

@Service
public abstract class AdapterTemplate implements PriceAdapter {

    @Override
    public PriceResource getPrices() {
        return this.findPricesFromProvider();
    }

    @Override
    public abstract PriceProvider getProvider();

    protected abstract PriceResource findPricesFromProvider();

}
