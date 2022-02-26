package com.jh.stock.domain.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SRimCreateParam {
    private String ticker;
    private BigDecimal persistenceFactor;
    private BigDecimal requiredYield;
    private BigDecimal expectedRoe;
    private long latestShareOfControllingShareholder;
    private long numberOfIssuedShares;
    private int latestYear;
}
