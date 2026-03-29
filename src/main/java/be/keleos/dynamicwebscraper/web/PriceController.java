package be.keleos.dynamicwebscraper.web;

import be.keleos.dynamicwebscraper.model.Highlight;
import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceProvider;
import be.keleos.dynamicwebscraper.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping("{provider}/prices")
    public PriceResource dynamicPrices(@PathVariable String provider,
                                       @RequestParam(required = false) String fallbackProvider) {
        var fallbackPriceProvider =
                fallbackProvider == null ? null:
                PriceProvider.getProvider(fallbackProvider);

        return priceService.getPrices(
                PriceProvider.getProvider(provider),
                fallbackPriceProvider
        );
    }

    @GetMapping("{provider}/highlights")
    public Highlight dynamicHighlights(
            @PathVariable String provider,
            @RequestParam(required = false) String fallbackProvider,
            @RequestParam(value = "datetime", required = false) LocalDateTime time) {

        var fallbackPriceProvider =
                fallbackProvider == null ? null:
                PriceProvider.getProvider(fallbackProvider);

        return priceService.getHighlight(
                PriceProvider.getProvider(provider),
                fallbackPriceProvider,
                time
        );
    }
}
