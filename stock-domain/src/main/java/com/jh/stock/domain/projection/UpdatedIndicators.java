package com.jh.stock.domain.projection;

import com.jh.stock.domain.IndicatorInterval;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedIndicators {
    private String ticker;
    private String type;
    private IndicatorInterval issuanceCycle;

    @Override
    public int hashCode() {
        return Objects.hash(ticker, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UpdatedIndicators) {
            UpdatedIndicators updatedIndicators = (UpdatedIndicators) obj;

            return this.ticker.equals(updatedIndicators.getTicker()) && this.type.equals(updatedIndicators.getType()) && this.issuanceCycle.equals(updatedIndicators.getIssuanceCycle());
        }

        return false;
    }
}
