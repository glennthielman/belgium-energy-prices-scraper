package be.keleos.dynamicwebscraper.adaptor.luminus;

import be.keleos.dynamicwebscraper.model.Price;
import be.keleos.dynamicwebscraper.model.PriceResource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static be.keleos.dynamicwebscraper.config.CachingConfiguration.LUMINUS_PRICES_CACHE_NAME;

@Service
@Slf4j
public class LuminusPriceAdaptor {

    private static final String URL = "https://my.luminusbusiness.be/market-info/nl/dynamic-prices/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";
    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    private static final String ACCEPT_LANGUAGE_VALUE = "*";

    private static final int CURRENT_DAY_TABLE_INDEX = 1;
    private static final int DAY_AHEAD_TABLE_INDEX = 2;

    @Cacheable(LUMINUS_PRICES_CACHE_NAME)
    public PriceResource getPrices() {
        try {
            var doc = getDocument();
            var date = getDate(doc, false);
            var dateAhead = getDate(doc, true);

            var pricesDayOne = mapPrices(doc, date, false);
            var pricesDayTwo = mapPrices(doc, dateAhead, true);

            return new PriceResource()
                    .setPrices(pricesDayOne)
                    .setPricesDayAhead(pricesDayTwo);
        } catch (IOException e) {
            log.error("Cannot get LuminusPrices from web", e);
        }
        return null;
    }

    @CacheEvict(value = LUMINUS_PRICES_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void evictCache() {
        log.debug("Evicting {} from web", LUMINUS_PRICES_CACHE_NAME);
    }

    private Document getDocument() throws IOException {
        return Jsoup.connect(URL)
                .userAgent(USER_AGENT)
                .header(ACCEPT_LANGUAGE_HEADER, ACCEPT_LANGUAGE_VALUE)
                .get();
    }

    private LocalDate getDate(Document doc, boolean dayAhead) {
        var index = dayAhead ? DAY_AHEAD_TABLE_INDEX : CURRENT_DAY_TABLE_INDEX;
        var tableheadElements = doc.select("thead th");
        var date1 = tableheadElements.get(index).text()
                .substring(17); //remove "Belpex H (€/MWh) "
        return LocalDate.parse(date1, DateTimeFormatter.ofPattern("dd/MM/’yy"));
    }

    private List<Price> mapPrices(Document doc, LocalDate date, boolean dayAhead) throws IOException {
        var elements = doc.select("tbody tr");
        return elements.stream()
                .map(element -> mapElementToPrice(element, date, dayAhead))
                .toList();
    }

    private Price mapElementToPrice(Element element, LocalDate date, boolean dayAhead) {
        var index = dayAhead ? DAY_AHEAD_TABLE_INDEX : CURRENT_DAY_TABLE_INDEX;
        var td = element.select("td");

        var time = td.getFirst().text()
                .replaceAll("\\.", ":")
                .substring(0, 5);

        var price = td.get(index).text();
        price = price.equalsIgnoreCase("tbd") ?
                "0" :
                price.replaceAll("€", "");

        var startDateTime = LocalDateTime.of(date, LocalTime.parse(time));
        return new Price()
                .setStartTime(startDateTime)
                .setEndTime(startDateTime.plusHours(1))
                .setPrice(new BigDecimal(price));
    }

}
