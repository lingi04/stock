package com.jh.stock.domain;

import com.jh.stock.domain.param.IndicatorsParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("CONFIRMED")
@AllArgsConstructor
public class ConfirmedIndicators extends Indicators {
    @Builder(access = AccessLevel.PROTECTED)
    public ConfirmedIndicators(long id, MyStock myStock, String ticker, IndicatorType indicatorType, Integer businessYear, Integer businessMonth, ReportType reportType, boolean isExpectedStatements, IndicatorInterval issuanceCycle, Long totalRevenue, Long operatingProfit, Long netProfit, BigDecimal operatingMargin, BigDecimal netMargin, BigDecimal roe, BigDecimal liabilitiesRatio, BigDecimal quickRatio, BigDecimal reservationRatio, Integer eps, BigDecimal per, BigDecimal bps, BigDecimal pbr, Integer dividend, BigDecimal marketPriceDividend, BigDecimal dividendPayoutRatio, Long netProfitOfControllingShareHolder, Long netProfitOfNonControllingShareHolder, Long totalAssets, Long totalLiabilities, Long totalEquity, Long shareOfControllingShareholder, Long shareOfNonControllingShareholder, Long capital, Long netMarginOfControllingShareholder, Long numberOfIssuedShares, Timestamp createTime, Timestamp updateTime) {
        super(id, myStock, ticker, indicatorType, businessYear, businessMonth, reportType, isExpectedStatements, issuanceCycle, totalRevenue, operatingProfit, netProfit, operatingMargin, netMargin, roe, liabilitiesRatio, quickRatio, reservationRatio, eps, per, bps, pbr, dividend, marketPriceDividend, dividendPayoutRatio, netProfitOfControllingShareHolder, netProfitOfNonControllingShareHolder, totalAssets, totalLiabilities, totalEquity, shareOfControllingShareholder, shareOfNonControllingShareholder, capital, netMarginOfControllingShareholder, numberOfIssuedShares, createTime, updateTime);
    }

    public static Indicators from(IndicatorsParam param) {
        return ConfirmedIndicators.builder().issuanceCycle(IndicatorInterval.QUARTER)
            .reportType(param.getReportType())
            .indicatorType(IndicatorType.CONFIRMED)
            .businessYear(param.getBusinessYear())
            .businessMonth(param.getBusinessMonth())
            .issuanceCycle(param.getIssuanceCycle())
            .ticker(param.getTicker())
            .operatingProfit(param.getOperatingProfit())
            .totalRevenue(param.getTotalRevenue())
            .operatingMargin(param.getOperatingMargin())
            .netProfit(param.getNetProfit())
            .operatingMargin(param.getOperatingMargin())
            .netMargin(param.getNetMargin())
            .roe(param.getRoe())
            .liabilitiesRatio(param.getLiabilitiesRatio())
            .quickRatio(param.getQuickRatio())
            .reservationRatio(param.getReservationRatio())
            .eps(param.getEps())
            .per(param.getPer())
            .bps(param.getBps())
            .pbr(param.getPbr())
            .dividend(param.getDividend())
            .marketPriceDividend(param.getMarketPriceDividend())
            .dividendPayoutRatio(param.getDividendPayoutRatio())
            .netProfitOfControllingShareHolder(param.getNetProfitOfControllingShareHolder())
            .netProfitOfNonControllingShareHolder(param.getNetProfitOfNonControllingShareHolder())
            .totalAssets(param.getTotalAssets())
            .totalLiabilities(param.getTotalLiabilities())
            .totalEquity(param.getTotalEquity())
            .shareOfControllingShareholder(param.getShareOfControllingShareholder())
            .shareOfNonControllingShareholder(param.getShareOfNonControllingShareholder())
            .capital(param.getCapital())
            .netMarginOfControllingShareholder(param.getNetMarginOfControllingShareholder())
            .numberOfIssuedShares(param.getNumberOfIssuedShares())
            .build();
    }
}
