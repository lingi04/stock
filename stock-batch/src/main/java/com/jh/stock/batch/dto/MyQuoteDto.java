package com.jh.stock.batch.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyQuoteDto {
    private long id;
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
    private BigDecimal perOf12Month;
    private BigDecimal perOfSector;
}
