package be.keleos.dynamicwebscraper.adaptor.elia.model;

import be.keleos.dynamicwebscraper.model.Price;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Getter
@Setter
public class OutEliaPrice {

    private LocalDateTime dateTime;
    private BigDecimal price;
    private Boolean isVisible;

    public Price toPrice() {
        return new Price()
                .setPrice(price)
                .setStartTime(dateTime.plusHours(2))
                .setEndTime(dateTime.plusHours(2).plusMinutes(15));
    }
}
