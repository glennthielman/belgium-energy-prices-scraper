package be.keleos.dynamicwebscraper.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfiguration {

    public final static String CREG_PRICES_CACHE_NAME = "CregPricesCache";
    public final static String PRICE_CACHE_NAME = "priceCache";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                CREG_PRICES_CACHE_NAME,
                PRICE_CACHE_NAME
        );
    }

}
