package be.keleos.dynamicwebscraper.adaptor.creg;

import be.keleos.dynamicwebscraper.model.FixedPrice;
import be.keleos.dynamicwebscraper.model.FixedQuarterlyPrice;
import be.keleos.dynamicwebscraper.model.FixedQuarterlyPriceResource;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import static be.keleos.dynamicwebscraper.config.CachingConfiguration.CREG_PRICES_CACHE_NAME;

@Service
@Slf4j
public class CregFixedPriceAdapter {

    private static final String URL = "https://www.creg.be/nl/consumenten/prijzen-en-tarieven/creg-tarief-voor-terugbetaling-thuisladen-bedrijfswagens";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";
    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    private static final String ACCEPT_LANGUAGE_VALUE = "*";

    private static final int COLUMN_INDEX_FLANDERS = 0;
    private static final int COLUMN_INDEX_BRUSSELS = 1;
    private static final int COLUMN_INDEX_WALLONIA = 2;

    private static final int QUARTER_SUBSTRING_START = 1;
    private static final int QUARTER_SUBSTRING_END = 2;
    private static final int YEAR_SUBSTRING_START = 3;
    private static final int YEAR_SUBSTRING_END = 7;

    @Cacheable(CREG_PRICES_CACHE_NAME)
    public FixedQuarterlyPriceResource getFixedQuarterlyPrices() throws IOException {
        var doc = getDocument();
        return new FixedQuarterlyPriceResource()
                .setPrices(toFixedQuarterlyPrices(doc));
    }

    @CacheEvict(value = CREG_PRICES_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void evictCache() {
        log.debug("Evicting {} from web", CREG_PRICES_CACHE_NAME);
    }

    private Document getDocument() throws IOException {
        return Jsoup.connect(URL)
                .userAgent(USER_AGENT)
                .header(ACCEPT_LANGUAGE_HEADER, ACCEPT_LANGUAGE_VALUE)
                .get();
    }

    private List<FixedQuarterlyPrice> toFixedQuarterlyPrices(Document doc) {
        var elements = doc.select("tbody tr");
        return elements.stream()
                .map(this::mapElementToFixedQuarterlyPrice)
                .toList();
    }

    private FixedQuarterlyPrice mapElementToFixedQuarterlyPrice(Element element) {
        var quarter = element.select("th").getFirst().text();

        var pricesTd = element.select("td");
        var priceFlanders = pricesTd.get(COLUMN_INDEX_FLANDERS).text().replaceAll(",", ".");
        var priceBrussels = pricesTd.get(COLUMN_INDEX_BRUSSELS).text().replaceAll(",", ".");
        var priceWallonia = pricesTd.get(COLUMN_INDEX_WALLONIA).text().replaceAll(",", ".");

        return new FixedQuarterlyPrice()
                .setQuarter(Integer.parseInt(quarter.substring(QUARTER_SUBSTRING_START, QUARTER_SUBSTRING_END)))
                .setYear(Integer.parseInt(quarter.substring(YEAR_SUBSTRING_START,  YEAR_SUBSTRING_END)))
                .setPriceFlanders(new FixedPrice().setPrice(new BigDecimal(priceFlanders)))
                .setPriceBrussels(new FixedPrice().setPrice(new BigDecimal(priceBrussels)))
                .setPriceWallonia(new FixedPrice().setPrice(new BigDecimal(priceWallonia)));
    }

}
