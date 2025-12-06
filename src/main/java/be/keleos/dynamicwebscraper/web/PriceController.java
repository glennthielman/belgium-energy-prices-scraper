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

@RestController
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping("{provider}/prices")
    public PriceResource dynamicPrices(@PathVariable String provider) {
        return priceService.getPrices(
                PriceProvider.getProvider(provider)
        );
    }

    @GetMapping("{provider}/highlights")
    public Highlight dynamicHighlights(
            @PathVariable String provider,
            @RequestParam(value = "datetime", required = false) LocalDateTime time) {
        return priceService.getHighlight(
                PriceProvider.getProvider(provider),
                time
        );
    }
}
