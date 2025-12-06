package be.keleos.dynamicwebscraper.service;

import be.keleos.dynamicwebscraper.adaptor.PriceAdapterService;
import be.keleos.dynamicwebscraper.model.Calculations;
import be.keleos.dynamicwebscraper.model.Highlight;
import be.keleos.dynamicwebscraper.model.Price;
import be.keleos.dynamicwebscraper.model.PriceResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceAdapterService priceAdapterService;

    public PriceResource getPrices(PriceProvider provider) {
        return priceAdapterService.getPrices(provider);
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
                .setAveragePriceKwH(getAveragePriceKwh(provider))
                .setAveragePriceMwH(getAveragePriceMph(provider));
    }

    public Price getCurrentPrice(PriceProvider provider, LocalDateTime dateTime) {
        return dateTime == null ? null :
                Stream.concat(getPrices(provider).getPrices().stream(), getPrices(provider).getPricesDayAhead().stream())
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

    public BigDecimal getAveragePriceKwh(PriceProvider provider) {
        var prices = getPrices(provider);
        var sum = prices.getPrices().stream()
                .map(Price::getPriceKwH)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(prices.getPrices().size()), RoundingMode.HALF_UP);
    }

    public BigDecimal getAveragePriceMph(PriceProvider provider) {
        var prices = getPrices(provider);
        var sum = prices.getPrices().stream()
                .map(Price::getPriceMwH)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(prices.getPrices().size()), RoundingMode.HALF_UP);
    }
}
