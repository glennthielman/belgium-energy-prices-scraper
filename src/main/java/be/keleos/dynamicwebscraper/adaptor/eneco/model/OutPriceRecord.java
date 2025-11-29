package be.keleos.dynamicwebscraper.adaptor.eneco.model;

import be.keleos.dynamicwebscraper.model.Price;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Accessors(chain = true)
@Getter
@Setter
public class OutPriceRecord {

    private LocalDate date;
    private LocalTime time;
    private BigDecimal price;

    public Price toPrice() {
        return new Price()
                .setStartTime(LocalDateTime.of(date, time))
                .setEndTime(LocalDateTime.of(date, time).plusHours(1))
                .setPrice(price);
    }

}
