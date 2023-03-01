package com.jh.stock.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class IndicatorReportTypeTest {
    @Test
    void formatting_number_test() {
        String ret = IndicatorReportType.formatNumber(14321231L);
        assertThat(ret).isEqualTo("14,321,231");
    }

    @Test
    void formatting_number_double_test() {
        String ret = IndicatorReportType.formatNumber(14321231.3242d);
        assertThat(ret).isEqualTo("14,321,231.32");
    }

    @Test
    void formatting_number_double_test2() {
        String ret = IndicatorReportType.formatNumber(14321231d);
        assertThat(ret).isEqualTo("14,321,231.00");
    }

    @Test
    void formatting_number_bigDecimal_test() {
        String ret = IndicatorReportType.formatNumber(new BigDecimal("123456.342"));
        assertThat(ret).isEqualTo("123,456.34");
    }
}
