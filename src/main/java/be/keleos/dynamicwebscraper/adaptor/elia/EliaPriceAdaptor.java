package be.keleos.dynamicwebscraper.adaptor.elia;

import be.keleos.dynamicwebscraper.adaptor.AdapterTemplate;
import be.keleos.dynamicwebscraper.adaptor.elia.model.OutEliaPrice;
import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static be.keleos.dynamicwebscraper.service.PriceProvider.ELIA;

@Component
@Slf4j
public class EliaPriceAdaptor extends AdapterTemplate {

    private static final String URL = "https://griddata.elia.be/eliabecontrols.prod/interface/Interconnections/daily/auctionresultsqh/%s";
    private static final RestClient EliaClient = RestClient.create();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PriceProvider getProvider() {
        return ELIA;
    }

    @Override
    protected PriceResource findPricesFromProvider() {
        var today = LocalDate.now();
        var tomorrow = today.plusDays(1);

        var pricesToday = Stream.of(requestPrices(today))
                .map(OutEliaPrice::toPrice)
                .toList();

        var pricesTomorrow = Stream.of(requestPrices(tomorrow))
                .map(OutEliaPrice::toPrice)
                .toList();

        return new PriceResource()
                .setPrices(pricesToday)
                .setPricesDayAhead(pricesTomorrow);
    }

    private OutEliaPrice[] requestPrices(LocalDate startDate) {
        return EliaClient.get()
                .uri(String.format(URL, startDate))
                .retrieve()
                .body(OutEliaPrice[].class);
    }
}
