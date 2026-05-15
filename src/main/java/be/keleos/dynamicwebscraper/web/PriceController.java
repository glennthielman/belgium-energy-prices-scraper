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

    @GetMapping("/prices")
    public PriceResource getPrices() {
        return priceService.getPrices();
    }

    @GetMapping("/highlights")
    public Highlight getHighlights(
            @RequestParam(value = "datetime", required = false) LocalDateTime start) {
        return priceService.getHighlight(start);
    }
}
