package be.keleos.dynamicwebscraper.web;

import be.keleos.dynamicwebscraper.adaptor.creg.CregFixedPriceAdapter;
import be.keleos.dynamicwebscraper.model.FixedQuarterlyPrice;
import be.keleos.dynamicwebscraper.model.FixedQuarterlyPriceResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

import static java.time.temporal.IsoFields.QUARTER_OF_YEAR;

@RestController
@RequiredArgsConstructor
@RequestMapping("creg")
public class CregController {

    private final CregFixedPriceAdapter cregFixedPriceAdapter;

    @GetMapping("prices")
    public FixedQuarterlyPriceResource getQuarterlyFixedPrices() throws IOException {
        return cregFixedPriceAdapter.getFixedQuarterlyPrices();
    }

    @GetMapping("prices/current")
    public FixedQuarterlyPrice getCurrentQuarterlyPrice() throws IOException {
        var localDate = LocalDate.now();
        return cregFixedPriceAdapter.getFixedQuarterlyPrices()
                .getPrices().stream()
                .filter(fixedQuarterlyPrice -> fixedQuarterlyPrice.getYear() == localDate.getYear())
                .filter(fixedQuarterlyPrice -> fixedQuarterlyPrice.getQuarter() == localDate.get(QUARTER_OF_YEAR))
                .findFirst()
                .orElse(null);
    }
}
