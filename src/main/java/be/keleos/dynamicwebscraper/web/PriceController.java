package be.keleos.dynamicwebscraper.web;

import be.keleos.dynamicwebscraper.model.Highlight;
import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceProvider;
import be.keleos.dynamicwebscraper.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
    public PriceResource getPrices(
            @RequestParam(value = "timeInterval", defaultValue="hourly") String timeInterval) {
        return priceService.getPrices(timeInterval);
    }

    @GetMapping("/highlights")
    public Highlight getHighlights(
            @RequestParam(value = "timeInterval", defaultValue="hourly") String timeInterval,
            @RequestParam(value = "datetime", required = false) LocalDateTime start) {
        return priceService.getHighlight(timeInterval, start);
    }
}
