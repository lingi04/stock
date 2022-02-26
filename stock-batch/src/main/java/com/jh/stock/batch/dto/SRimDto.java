package com.jh.stock.batch.dto;

import com.jh.stock.domain.Trend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SRimDto {
    private String ticker;
    private String year;
    private Trend yieldTrend;
    private Trend yieldGrowth;
    private BigDecimal expectedRoe;
    private BigDecimal persistenceFactor;
    private BigDecimal expectedYield;
    private BigDecimal excessMargin;
    private BigDecimal netProfitOfControllingShareHolder;
    private BigDecimal shareOfControllingShareholder;
    private BigDecimal pvOfRi;
    private BigDecimal properPrice;
}
