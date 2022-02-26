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
@DiscriminatorValue("EXPECTED")
@AllArgsConstructor
public class ExpectedIndicators extends Indicators {
    @Builder(access = AccessLevel.PROTECTED)
    public ExpectedIndicators(long id, MyStock myStock, String ticker, IndicatorType indicatorType, Integer businessYear, Integer businessMonth, ReportType reportType, boolean isExpectedStatements, IndicatorInterval issuanceCycle, Long totalRevenue, Long operatingProfit, Long netProfit, BigDecimal operatingMargin, BigDecimal netMargin, BigDecimal roe, BigDecimal liabilitiesRatio, BigDecimal quickRatio, BigDecimal reservationRatio, Integer eps, BigDecimal per, BigDecimal bps, BigDecimal pbr, Integer dividend, BigDecimal marketPriceDividend, BigDecimal dividendPayoutRatio, Long netProfitOfControllingShareHolder, Long netProfitOfNonControllingShareHolder, Long totalAssets, Long totalLiabilities, Long totalEquity, Long shareOfControllingShareholder, Long shareOfNonControllingShareholder, Long capital, Long netMarginOfControllingShareholder, Long numberOfIssuedShares, Timestamp createTime, Timestamp updateTime) {
        super(id, myStock, ticker, indicatorType, businessYear, businessMonth, reportType, isExpectedStatements, issuanceCycle, totalRevenue, operatingProfit, netProfit, operatingMargin, netMargin, roe, liabilitiesRatio, quickRatio, reservationRatio, eps, per, bps, pbr, dividend, marketPriceDividend, dividendPayoutRatio, netProfitOfControllingShareHolder, netProfitOfNonControllingShareHolder, totalAssets, totalLiabilities, totalEquity, shareOfControllingShareholder, shareOfNonControllingShareholder, capital, netMarginOfControllingShareholder, numberOfIssuedShares, createTime, updateTime);
    }

    public static Indicators from(IndicatorsParam indicatorsParam) {

        return ExpectedIndicators.builder()
            .reportType(indicatorsParam.getReportType())
            .indicatorType(IndicatorType.EXPECTED)
            .businessYear(indicatorsParam.getBusinessYear())
            .businessMonth(indicatorsParam.getBusinessMonth())
            .issuanceCycle(indicatorsParam.getIssuanceCycle())
            .ticker(indicatorsParam.getTicker())
            .operatingProfit(indicatorsParam.getOperatingProfit())
            .totalRevenue(indicatorsParam.getTotalRevenue())
            .operatingMargin(indicatorsParam.getOperatingMargin())
            .netProfit(indicatorsParam.getNetProfit())
            .operatingMargin(indicatorsParam.getOperatingMargin())
            .netMargin(indicatorsParam.getNetMargin())
            .roe(indicatorsParam.getRoe())
            .liabilitiesRatio(indicatorsParam.getLiabilitiesRatio())
            .quickRatio(indicatorsParam.getQuickRatio())
            .reservationRatio(indicatorsParam.getReservationRatio())
            .eps(indicatorsParam.getEps())
            .per(indicatorsParam.getPer())
            .bps(indicatorsParam.getBps())
            .pbr(indicatorsParam.getPbr())
            .dividend(indicatorsParam.getDividend())
            .marketPriceDividend(indicatorsParam.getMarketPriceDividend())
            .dividendPayoutRatio(indicatorsParam.getDividendPayoutRatio())
            .netProfitOfControllingShareHolder(indicatorsParam.getNetProfitOfControllingShareHolder())
            .netProfitOfNonControllingShareHolder(indicatorsParam.getNetProfitOfNonControllingShareHolder())
            .totalAssets(indicatorsParam.getTotalAssets())
            .totalLiabilities(indicatorsParam.getTotalLiabilities())
            .totalEquity(indicatorsParam.getTotalEquity())
            .shareOfControllingShareholder(indicatorsParam.getShareOfControllingShareholder())
            .shareOfNonControllingShareholder(indicatorsParam.getShareOfNonControllingShareholder())
            .capital(indicatorsParam.getCapital())
            .netMarginOfControllingShareholder(indicatorsParam.getNetMarginOfControllingShareholder())
            .build();
    }
}
