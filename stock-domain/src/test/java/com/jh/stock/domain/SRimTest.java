package com.jh.stock.domain;

import com.jh.stock.domain.param.SRimCreateParam;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SRimTest {
    ///////////////////////////////////////////////
    //               calcSRim test               //
    ///////////////////////////////////////////////
    @Test
    public void calcSRim_test() {
        BigDecimal requiredYield = new BigDecimal("8.05");
        BigDecimal expectedRoe = new BigDecimal("8.69");

        SRim expected1 = SRim.builder()
            .properPrice(BigDecimal.valueOf(47578))
            .pvOfRi(new BigDecimal("4481.88"))
            .excessMargin(new BigDecimal("0.64"))
            .expectedYield(new BigDecimal("8.05"))
            .netProfitOfControllingShareHolder(new BigDecimal("13552.78"))
            .persistenceFactor(new BigDecimal("1.00"))
            .shareOfControllingShareholder(new BigDecimal("169511.18"))
            .year(2019)
            .build();
        SRim expected2 = SRim.builder()
            .excessMargin(new BigDecimal("0.22"))
            .netProfitOfControllingShareHolder(new BigDecimal("12628.49"))
            .persistenceFactor(new BigDecimal("0.90"))
            .properPrice(BigDecimal.valueOf(46405))
            .pvOfRi(new BigDecimal("2554.55"))
            .shareOfControllingShareholder(new BigDecimal("165330.93"))
            .expectedYield(new BigDecimal("8.05"))
            .year(2019)
            .build();
        SRim expected3 = SRim.builder()
            .excessMargin(new BigDecimal("0.06"))
            .netProfitOfControllingShareHolder(new BigDecimal("12235.32"))
            .persistenceFactor(new BigDecimal("0.80"))
            .properPrice(BigDecimal.valueOf(45779))
            .pvOfRi(new BigDecimal("1527.06"))
            .shareOfControllingShareholder(new BigDecimal("163102.40"))
            .expectedYield(new BigDecimal("8.05"))
            .year(2019)
            .build();

        assertThat(
            SRim.from(SRimCreateParam.builder()
                .persistenceFactor(BigDecimal.ONE)
                .requiredYield(requiredYield)
                .expectedRoe(expectedRoe)
                .latestShareOfControllingShareholder(73672l)
                .numberOfIssuedShares(164263000l)
                .latestYear(2019)
                .build()
            )
        )
            .usingRecursiveComparison()
            .isEqualTo(expected1);
        assertThat(
            SRim.from(SRimCreateParam.builder()
                .persistenceFactor(new BigDecimal("0.90"))
                .requiredYield(requiredYield)
                .expectedRoe(expectedRoe)
                .latestShareOfControllingShareholder(73672l)
                .numberOfIssuedShares(164263000l)
                .latestYear(2019)
                .build()
            )
        )
            .usingRecursiveComparison()
            .isEqualTo(expected2);
        assertThat(
            SRim.from(SRimCreateParam.builder()
                .persistenceFactor(new BigDecimal("0.80"))
                .requiredYield(requiredYield)
                .expectedRoe(expectedRoe)
                .latestShareOfControllingShareholder(73672l)
                .numberOfIssuedShares(164263000l)
                .latestYear(2019)
                .build()
            )
        )
            .usingRecursiveComparison()
            .isEqualTo(expected3);
    }
}
