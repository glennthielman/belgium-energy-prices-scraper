package be.keleos.dynamicwebscraper.adaptor.eneco;

import be.keleos.dynamicwebscraper.adaptor.eneco.model.OutEnecoPrices;
import be.keleos.dynamicwebscraper.model.PriceResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static be.keleos.dynamicwebscraper.config.CachingConfiguration.ENECO_PRICES_CACHE_NAME;

@Service
@Slf4j
public class EnecoPriceAdaptor {

    private static final String URL = "https://api-prd.be-digitalcore.enecogroup.com/eneco-be/xapi/site/api/v1/pricing/dynamic?startDate=%s&endDate=%s&aggregation=hourly";
    private static final RestClient EnecoClient = RestClient.create();

    @Cacheable(ENECO_PRICES_CACHE_NAME)
    public PriceResource getPrices() {
        var today = LocalDate.now();
        var tomorrow = today.plusDays(1);

        var outPrices = requestPrices(today, tomorrow)
                .getData()
                .toPrices();

        return new PriceResource()
                .setPrices(outPrices.subList(0, 23))
                .setPricesDayAhead(outPrices.size() > 24 ? outPrices.subList(24, outPrices.size()) : new ArrayList<>());
    }

    @CacheEvict(value = ENECO_PRICES_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void evictCache() {
        log.debug("Evicting {} from web", ENECO_PRICES_CACHE_NAME);
    }

    private OutEnecoPrices requestPrices(LocalDate startDate, LocalDate endDate) {
        return EnecoClient.get()
                .uri(String.format(URL, startDate, endDate))
                .retrieve()
                .body(OutEnecoPrices.class);
    }



}
