package com.jh.stock.domain;

import com.jh.stock.domain.param.SRimCreateParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SRim {
    private int year;
    private String ticker;
    @Builder.Default
    private Trend yieldTrend = Trend.NO_TREND;
    @Builder.Default
    private Trend yieldGrowth = Trend.NO_TREND;
    private BigDecimal requiredRoe;
    private BigDecimal persistenceFactor;
    private BigDecimal expectedYield;
    private BigDecimal excessMargin;
    private BigDecimal netProfitOfControllingShareHolder;
    private BigDecimal shareOfControllingShareholder;
    private BigDecimal pvOfRi;
    private BigDecimal properPrice;

    public static SRim from(SRimCreateParam param) {
        BigDecimal pvOfRi = BigDecimal.ZERO;
        BigDecimal shareOfControllingShareholder = new BigDecimal(param.getLatestShareOfControllingShareholder());
        BigDecimal netProfitOfControllingShareHolder = null;
        BigDecimal excessProfit;
        BigDecimal requiredYield = param.getRequiredYield();
        BigDecimal expectedRoe = param.getExpectedRoe();
        BigDecimal presentValueConversionFactor = BigDecimal.ONE.add(requiredYield.divide(BigDecimal.valueOf(100)));
        BigDecimal presentValueConversion = BigDecimal.ONE.multiply(presentValueConversionFactor);

        // 초과이익률
        BigDecimal excessMargin = expectedRoe.subtract(requiredYield);
        for (int i = 0; i < 10; i++) {
            excessMargin = excessMargin.multiply(param.getPersistenceFactor());
            expectedRoe = excessMargin.add(requiredYield).setScale(2, RoundingMode.FLOOR);
            // 지배주주순이익
            netProfitOfControllingShareHolder = shareOfControllingShareholder.multiply(expectedRoe.divide(new BigDecimal(100)));
            // 초과이익 = 당해년도 지배주주순이익 - (전년도 지배주주 지분 * 요구수익률)
            excessProfit = netProfitOfControllingShareHolder.subtract(shareOfControllingShareholder.multiply(requiredYield).divide(new BigDecimal(100)));
            // 지배주주지분 = 전년도 지배주주지분 + 당해년도지배주주순이익
            shareOfControllingShareholder = shareOfControllingShareholder.add(netProfitOfControllingShareHolder);
            // pv of ri = 전년도 pvOfRi + (초과이익 * 현재 이익 환산 계수)
            pvOfRi = pvOfRi.add(excessProfit.divide(presentValueConversion, 2, RoundingMode.FLOOR));
            // 현재이익 환산 계수 = 전년도 계수 * 환산 팩터
            presentValueConversion = presentValueConversion.multiply(presentValueConversionFactor).setScale(5, RoundingMode.FLOOR);
        }

        return SRim.builder()
            .year(param.getLatestYear())
            .expectedYield(expectedRoe)
            .ticker(param.getTicker())
            .persistenceFactor(new BigDecimal(param.getPersistenceFactor().setScale(2, RoundingMode.FLOOR).toString()))
            .expectedYield(new BigDecimal(requiredYield.setScale(2, RoundingMode.FLOOR).toString()))
            .excessMargin(new BigDecimal(excessMargin.setScale(2, RoundingMode.FLOOR).toString()))
            .netProfitOfControllingShareHolder(new BigDecimal(netProfitOfControllingShareHolder.setScale(2, RoundingMode.FLOOR).toString()))
            .shareOfControllingShareholder(new BigDecimal(shareOfControllingShareholder.setScale(2, RoundingMode.FLOOR).toString()))
            .pvOfRi(new BigDecimal(pvOfRi.setScale(2, RoundingMode.FLOOR).toString()))
            .properPrice(
                pvOfRi.setScale(2, RoundingMode.FLOOR).add(new BigDecimal(param.getLatestShareOfControllingShareholder())).multiply(new BigDecimal(100000000))
                    .divide(new BigDecimal(param.getNumberOfIssuedShares()), 0, RoundingMode.FLOOR)
            )
            .build();
    }
}
