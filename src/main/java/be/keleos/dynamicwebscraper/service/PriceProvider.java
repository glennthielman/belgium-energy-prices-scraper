package be.keleos.dynamicwebscraper.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum PriceProvider {

    ENECO("eneco", 1),
    LUMINUS("luminus", 2),
    OCTA_PLUS("octaplus", 3);

    private final String providerName;
    private final Integer order;

    public static PriceProvider getProvider(String providerName) {
        return Arrays.stream(PriceProvider.values())
                .filter(provider -> provider.getProviderName().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No provider with name " + providerName));
    }

    public static List<PriceProvider> getProviderInOrder() {
        return Arrays.stream(PriceProvider.values())
                .sorted(Comparator.comparing(PriceProvider::getOrder))
                .toList();
    }

}
