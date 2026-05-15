package be.keleos.dynamicwebscraper.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static be.keleos.dynamicwebscraper.service.TimeInterval.HOURLY;
import static be.keleos.dynamicwebscraper.service.TimeInterval.QUARTERLY;

@Getter
@RequiredArgsConstructor
public enum PriceProvider {

    ENECO("eneco", 1, HOURLY),
    LUMINUS("luminus", 2, HOURLY),
    OCTA_PLUS("octaplus", 3, HOURLY),
    ELIA("elia", 4, QUARTERLY);

    private final String providerName;
    private final Integer order;
    private final TimeInterval timeInterval;

    public static PriceProvider getProvider(String providerName) {
        return Arrays.stream(PriceProvider.values())
                .filter(provider -> provider.getProviderName().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No provider with name " + providerName));
    }

    public static List<PriceProvider> getProviderInOrder(TimeInterval timeInterval) {
        return Arrays.stream(PriceProvider.values())
                .filter(provider -> provider.getTimeInterval() == timeInterval)
                .sorted(Comparator.comparing(PriceProvider::getOrder))
                .toList();
    }

}
