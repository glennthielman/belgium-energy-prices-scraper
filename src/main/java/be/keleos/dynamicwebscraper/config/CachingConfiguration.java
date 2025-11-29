package be.keleos.dynamicwebscraper.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfiguration {

    public final static String LUMINUS_PRICES_CACHE_NAME = "luminusPricesCache";
    public final static String OCTAPLUS_PRICES_CACHE_NAME = "octavePricesCache";
    public final static String ENECO_PRICES_CACHE_NAME = "EnecoPricesCache";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                LUMINUS_PRICES_CACHE_NAME,
                OCTAPLUS_PRICES_CACHE_NAME,
                ENECO_PRICES_CACHE_NAME
        );
    }

}
