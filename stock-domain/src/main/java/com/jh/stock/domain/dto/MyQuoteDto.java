package com.jh.stock.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyQuoteDto {
    private String ticker;
    private Timestamp tradeDate;
    private BigDecimal marketPrice;
    private BigDecimal fluctuation;
    private BigDecimal marketCapitalization;
    private BigDecimal yearHigh;
    private BigDecimal yearLow;
    private Long transactionVolume;
    private Integer transactionPrice;
    private BigDecimal shareOfForeignOwnership;
    private BigDecimal perOfSector;
}
