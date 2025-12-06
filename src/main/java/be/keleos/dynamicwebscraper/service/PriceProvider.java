package be.keleos.dynamicwebscraper.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PriceProvider {

    ENECO("eneco"),
    LUMINUS("luminus"),
    OCTA_PLUS("octaplus");

    private final String providerName;

    public static PriceProvider getProvider(String providerName) {
        return Arrays.stream(PriceProvider.values())
                .filter(provider -> provider.getProviderName().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No provider with name " + providerName));
    }

}
