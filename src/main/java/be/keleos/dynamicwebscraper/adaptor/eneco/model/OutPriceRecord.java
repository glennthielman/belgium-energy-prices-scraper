package be.keleos.dynamicwebscraper.adaptor.eneco.model;

import be.keleos.dynamicwebscraper.model.Price;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(date, time, price);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var priceRecord = (OutPriceRecord) o;
        return this.date.isEqual(priceRecord.date) && this.time.equals(priceRecord.getTime());
    }
}
