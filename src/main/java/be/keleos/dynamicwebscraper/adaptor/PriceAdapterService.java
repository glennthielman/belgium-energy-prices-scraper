package be.keleos.dynamicwebscraper.adaptor;

import be.keleos.dynamicwebscraper.model.PriceResource;
import be.keleos.dynamicwebscraper.service.PriceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static be.keleos.dynamicwebscraper.config.CachingConfiguration.PRICE_CACHE_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceAdapterService {

    private final List<PriceAdapter> priceAdapters;

    @Cacheable(value=PRICE_CACHE_NAME, key="#provider")
    public PriceResource getPrices(PriceProvider provider) {
        var adapter = priceAdapters
                .stream()
                .filter(priceAdapter -> priceAdapter.getProvider() == provider)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No PriceAdapter found for provider " + provider));
        return adapter.getPrices();
    }

    @CacheEvict(value = PRICE_CACHE_NAME, allEntries = true)
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void evictCache() {
        log.debug("Evicting {} from web", PRICE_CACHE_NAME);
    }
}
