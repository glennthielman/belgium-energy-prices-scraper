package be.keleos.dynamicwebscraper.adaptor.eneco;

import be.keleos.dynamicwebscraper.adaptor.AdapterTemplate;
import be.keleos.dynamicwebscraper.adaptor.eneco.model.OutEnecoPrices;
import be.keleos.dynamicwebscraper.model.Price;
import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static be.keleos.dynamicwebscraper.service.PriceProvider.ENECO;

@Component
@Slf4j
public class EnecoPriceAdaptor extends AdapterTemplate {

    private static final String URL = "https://api-prd.be-digitalcore.enecogroup.com/eneco-be/xapi/site/api/v1/pricing/dynamic?startDate=%s&endDate=%s&aggregation=hourly";
    private static final RestClient EnecoClient = RestClient.create();

    @Override
    public PriceProvider getProvider() {
        return ENECO;
    }

    @Override
    protected PriceResource findPricesFromProvider() {
        var today = LocalDate.now();
        var tomorrow = today.plusDays(1);

        var outPrices = requestPrices(today, tomorrow)
                .getData()
                .toPrices();

        return new PriceResource()
                .setPrices(outPrices.subList(0, 24))
                .setPricesDayAhead(outPrices.size() > 24 ? outPrices.subList(24, outPrices.size()) : new ArrayList<>());
    }

    private OutEnecoPrices requestPrices(LocalDate startDate, LocalDate endDate) {
        return EnecoClient.get()
                .uri(String.format(URL, startDate, endDate))
                .retrieve()
                .body(OutEnecoPrices.class);
    }
}
