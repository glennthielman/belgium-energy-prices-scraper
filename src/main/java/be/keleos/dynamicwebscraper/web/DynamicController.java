package be.keleos.dynamicwebscraper.web;

import be.keleos.dynamicwebscraper.model.Highlight;
import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class DynamicController {

    private final PriceService priceService;

    @GetMapping("/prices")
    public PriceResource dynamicPrices() throws IOException {
        return priceService.getPrices();
    }

    @GetMapping("/highlights")
    public Highlight dynamicHighlights(@RequestParam(value = "datetime", required = false) LocalDateTime time) throws IOException {
        return priceService.getHighlight(time);
    }
}
