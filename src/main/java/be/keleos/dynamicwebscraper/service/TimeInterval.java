package be.keleos.dynamicwebscraper.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TimeInterval {

    HOURLY("hourly"),
    QUARTERLY("quarterly");

    private final String name;

    public static TimeInterval getTimeInterval(String name) {
        return Arrays.stream(TimeInterval.values())
                .filter(timeInterval -> timeInterval.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
