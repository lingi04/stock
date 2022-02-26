package com.jh.stock.domain.param;

import com.jh.stock.domain.IndicatorInterval;
import com.jh.stock.domain.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class IndicatorsParam {
    private Integer businessYear;

    private Integer businessMonth;

    private IndicatorInterval issuanceCycle;

    private ReportType reportType;

    private String ticker;
    // 매출액
    private Long totalRevenue;
    // 영업이익
    private Long operatingProfit;
    // 순이익
    private Long netProfit;
    // 영업이익률
    private BigDecimal operatingMargin;
    // 순이익률
    private BigDecimal netMargin;
    // ROE
    private BigDecimal roe;
    // 부채비율
    private BigDecimal liabilitiesRatio;
    // 당좌비율
    private BigDecimal quickRatio;
    // 유보율
    private BigDecimal reservationRatio;
    // eps
    private Integer eps;
    // per
    private BigDecimal per;
    // bps
    private BigDecimal bps;
    // pbr
    private BigDecimal pbr;
    // 배당금
    private Integer dividend;
    // 시가배당률
    private BigDecimal marketPriceDividend;
    // 배당성향
    private BigDecimal dividendPayoutRatio;
    // 지배 주주 순이익
    private Long netProfitOfControllingShareHolder;
    // 비 지배 주주 순이익
    private Long netProfitOfNonControllingShareHolder;
    // 자산 총계
    private Long totalAssets;
    // 부채 총계
    private Long totalLiabilities;
    // 자본 총계
    private Long totalEquity;
    // 지배주주 지분
    private Long shareOfControllingShareholder;
    // 비지배주주 지분
    private Long shareOfNonControllingShareholder;
    // 자본금
    private Long capital;
    // 지배주주 순이익률
    private Long netMarginOfControllingShareholder;
    // 발행 주식 수
    private Long numberOfIssuedShares;
}
