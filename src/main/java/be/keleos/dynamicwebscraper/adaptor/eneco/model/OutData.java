package be.keleos.dynamicwebscraper.adaptor.eneco.model;

import be.keleos.dynamicwebscraper.model.Price;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
public class OutData {

    List<OutPriceRecord> records = new ArrayList<>();

    public List<Price> toPrices() {
        return records
                .stream()
                .map(OutPriceRecord::toPrice)
                .toList();
    }

}
