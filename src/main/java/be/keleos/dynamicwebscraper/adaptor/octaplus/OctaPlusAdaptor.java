package be.keleos.dynamicwebscraper.adaptor.octaplus;

import be.keleos.dynamicwebscraper.adaptor.octaplus.model.OutCotation;
import be.keleos.dynamicwebscraper.adaptor.octaplus.model.OutPrice;
import be.keleos.dynamicwebscraper.adaptor.octaplus.model.OutPriceResource;
import be.keleos.dynamicwebscraper.model.Price;
import be.keleos.dynamicwebscraper.model.PriceResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OctaPlusAdaptor {

    private static final String URL = "https://srv.octaplus.be/websiterest/GetTarDynCotations";
    private static final RestClient OctaPlusClient = RestClient.create();
    private static final HashMap<LocalDate, List<Price>> priceMapPerDate = new HashMap<>();

    @Cacheable("OctaPlusPricesCache")
    public PriceResource getPrices() {
        var outCotation = requestPrices();
        mapPricesAndSaveInMap(outCotation.getOutPriceResource());

        return new PriceResource()
                .setPrices(priceMapPerDate.getOrDefault(LocalDate.now(), null))
                .setPricesDayAhead(priceMapPerDate.getOrDefault(LocalDate.now().plusDays(1), null));
    }

    @CacheEvict(value = "OctaPlusPricesCache", allEntries = true)
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void evictCache() {
        log.debug("Evicting OctaPlusPricesCache from web");
        removeOldDates();
    }

    private void removeOldDates() {
        if(priceMapPerDate.size() > 2) {
            var minDate = Collections.min(priceMapPerDate.keySet());
            priceMapPerDate.remove(minDate);
            removeOldDates();
        }
    }

    private OutCotation requestPrices(){
        return OctaPlusClient.get()
                .uri(URL)
                .retrieve()
                .body(OutCotation.class);
    }

    private void mapPricesAndSaveInMap(OutPriceResource outPriceResource) {
        var date = LocalDate.parse(outPriceResource.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if(!priceMapPerDate.containsKey(date)) {
            var prices = outPriceResource.getPrices()
                    .stream()
                    .map(outPrice -> maptoPrice(date, outPrice))
                    .toList();
            priceMapPerDate.put(date, prices);
        }
    }

    private Price maptoPrice(LocalDate date, OutPrice outPrice) {
        var time = outPrice.getTime().substring(0, 2) + ":00";
        var startDateTime = LocalDateTime.of(date, LocalTime.parse(time));
        var price = outPrice.getPriceInMwH().replaceAll(",", ".");
        return new Price()
                .setStartTime(startDateTime)
                .setEndTime(startDateTime.plusHours(1))
                .setPrice(new BigDecimal(price));

    }
}
