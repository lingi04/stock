package com.jh.stock.domain;

import com.jh.stock.domain.dto.MyQuoteDto;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

@Getter
@Entity
@Table(name = "quote")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ticker;

    private Timestamp tradeDate;

    /**
     * 시황(Quote)
     */
    // 시장가
    private BigDecimal marketPrice;
    // 등락
    private BigDecimal fluctuation;
    // 시가총액
    private BigDecimal marketCapitalization;
    // 52주 최고 / 최저
    private BigDecimal yearHigh;
    private BigDecimal yearLow;
    // 거래량
    private Long transactionVolume;
    // 거래대금
    private Integer transactionPrice;
    // 외국인 보유 비중
    private BigDecimal shareOfForeignOwnership;
    private BigDecimal perOfSector;

    public String toStringMarketPriceAndFluctuation() {
        return marketPrice.setScale(0, RoundingMode.DOWN) + "(" + fluctuation.setScale(0, RoundingMode.DOWN) + ")";
    }

    public static MyQuote of(MyQuoteDto myQuoteDto) {
        return MyQuote.builder()
            .ticker(myQuoteDto.getTicker())
            .tradeDate(myQuoteDto.getTradeDate())
            .marketPrice(myQuoteDto.getMarketPrice())
            .fluctuation(myQuoteDto.getFluctuation())
            .marketCapitalization(myQuoteDto.getMarketCapitalization())
            .yearHigh(myQuoteDto.getYearHigh())
            .yearLow(myQuoteDto.getYearLow())
            .transactionVolume(myQuoteDto.getTransactionVolume())
            .transactionPrice(myQuoteDto.getTransactionPrice())
            .shareOfForeignOwnership(myQuoteDto.getShareOfForeignOwnership())
            .perOfSector(myQuoteDto.getPerOfSector())
            .build();
    }

    public static MyQuote emptyQuote() {
        return MyQuote.builder().marketPrice(BigDecimal.ZERO).fluctuation(BigDecimal.ZERO).build();
    }
}
