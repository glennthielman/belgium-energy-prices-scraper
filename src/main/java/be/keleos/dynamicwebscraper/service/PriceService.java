package be.keleos.dynamicwebscraper.service;

import be.keleos.dynamicwebscraper.adaptor.eneco.EnecoPriceAdaptor;
import be.keleos.dynamicwebscraper.adaptor.luminus.LuminusPriceAdaptor;
import be.keleos.dynamicwebscraper.adaptor.octaplus.OctaPlusAdaptor;
import be.keleos.dynamicwebscraper.model.Calculations;
import be.keleos.dynamicwebscraper.model.Highlight;
import be.keleos.dynamicwebscraper.model.Price;
import be.keleos.dynamicwebscraper.model.PriceResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final LuminusPriceAdaptor luminusPriceAdaptor;
    private final OctaPlusAdaptor octaPlusAdaptor;
    private final EnecoPriceAdaptor enecoPriceAdaptor;

    public PriceResource getPrices(PriceProvider provider) {
        return switch (provider) {
            case LUMINUS -> luminusPriceAdaptor.getPrices();
            case OCTA_PLUS -> octaPlusAdaptor.getPrices();
            case ENECO -> enecoPriceAdaptor.getPrices();
        };
    }

    public Highlight getHighlight(PriceProvider provider, LocalDateTime dateTime) {
        return new Highlight()
                .setCurrentPrice(getCurrentPrice(provider, dateTime))
                .setMinPrice(getMinPrice(provider))
                .setMaxPrice(getMaxPrice(provider))
                .setCalculations(getCalculations(provider));

    }

    public Calculations getCalculations(PriceProvider provider) {
        return new Calculations()
                .setAveragePriceKwH(getAvaragePriceKwh(provider))
                .setAveragePriceMwH(getAvaragePriceMwh(provider));
    }

    public Price getCurrentPrice(PriceProvider provider, LocalDateTime dateTime) {
        return dateTime == null ? null :
                getPrices(provider).getPrices().stream()
                .filter(price -> (dateTime.isAfter(price.getStartTime()) && dateTime.isBefore(price.getEndTime()) || dateTime.isEqual(price.getStartTime())))
                .findFirst()
                .orElse(null);
    }

    public Price getMinPrice(PriceProvider provider) {
        return getPrices(provider).getPrices().stream()
                .min(Comparator.comparing(Price::getPriceMwH))
                .orElse(null);
    }

    public Price getMaxPrice(PriceProvider provider) {
        return getPrices(provider).getPrices().stream()
                .max(Comparator.comparing(Price::getPriceMwH))
                .orElse(null);
    }

    public BigDecimal getAvaragePriceKwh(PriceProvider provider) {
        var prices = getPrices(provider);
        var sum = prices.getPrices().stream()
                .map(Price::getPriceKwH)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(prices.getPrices().size()));
    }

    public BigDecimal getAvaragePriceMwh(PriceProvider provider) {
        var prices = getPrices(provider);
        var sum = prices.getPrices().stream()
                .map(Price::getPriceMwH)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(prices.getPrices().size()));
    }
}
