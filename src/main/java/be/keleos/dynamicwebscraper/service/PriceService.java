package be.keleos.dynamicwebscraper.service;

import be.keleos.dynamicwebscraper.adaptor.LuminusPriceAdaptor;
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

@Service
@RequiredArgsConstructor
public class PriceService {

    private final LuminusPriceAdaptor luminusPriceAdaptor;

    public PriceResource getPrices() {
        return luminusPriceAdaptor.getPrices();
    }

    public Highlight getHighlight(LocalDateTime dateTime) {
        return new Highlight()
                .setCurrentPrice(getCurrentPrice(dateTime))
                .setMinPrice(getMinPrice())
                .setMaxPrice(getMaxPrice())
                .setCalculations(getCalculations());
    }

    public Calculations getCalculations() {
        return new Calculations()
                .setAveragePriceKwH(getAveragePriceKwh())
                .setAveragePriceMwH(getAveragePriceMwh());
    }

    public Price getCurrentPrice(LocalDateTime dateTime) {
        return dateTime == null ? null :
                getPrices().getPrices().stream()
                .filter(price -> (dateTime.isAfter(price.getStartTime()) && dateTime.isBefore(price.getEndTime()) || dateTime.isEqual(price.getStartTime())))
                .findFirst()
                .orElse(null);
    }

    public Price getMinPrice() {
        return getPrices().getPrices().stream()
                .min(Comparator.comparing(Price::getPriceMwH))
                .orElse(null);
    }

    public Price getMaxPrice() {
        return getPrices().getPrices().stream()
                .max(Comparator.comparing(Price::getPriceMwH))
                .orElse(null);
    }

    public BigDecimal getAveragePriceKwh() {
        var sum = getPrices().getPrices().stream()
                .map(Price::getPriceKwH)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(getPrices().getPrices().size()), RoundingMode.HALF_DOWN);
    }

    public BigDecimal getAveragePriceMwh() {
        var sum = getPrices().getPrices().stream()
                .map(Price::getPriceMwH)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(getPrices().getPrices().size()), RoundingMode.HALF_DOWN);
    }
}
