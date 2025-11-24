package be.keleos.dynamicwebscraper.web;

import be.keleos.dynamicwebscraper.model.Highlight;
import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static be.keleos.dynamicwebscraper.service.PriceProvider.OCTA_PLUS;

@RestController
@RequiredArgsConstructor
@RequestMapping("octaplus")
public class OctaPlusController {

    private final PriceService priceService;

    @GetMapping("/prices")
    public PriceResource getPrices() {
        return priceService.getPrices(OCTA_PLUS);
    }

    @GetMapping("/highlights")
    public Highlight getHighlights(@RequestParam(value = "datetime", required = false) LocalDateTime time) {
        return priceService.getHighlight(OCTA_PLUS, time);
    }

}
