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

import static be.keleos.dynamicwebscraper.service.PriceProvider.ENECO;

@RestController
@RequiredArgsConstructor
@RequestMapping("eneco")
public class EnecoController {

    private final PriceService priceService;

    @GetMapping("/prices")
    public PriceResource dynamicPrices() {
        return priceService.getPrices(ENECO);
    }

    @GetMapping("/highlights")
    public Highlight dynamicHighlights(@RequestParam(value = "datetime", required = false) LocalDateTime time) {
        return priceService.getHighlight(ENECO, time);
    }

}
