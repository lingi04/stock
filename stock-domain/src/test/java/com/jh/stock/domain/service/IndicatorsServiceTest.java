package com.jh.stock.domain.service;

import com.jh.stock.domain.*;
import com.jh.stock.domain.param.IndicatorsParam;
import com.jh.stock.domain.repository.IndicatorsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class IndicatorsServiceTest {
    private IndicatorsService indicatorsService;

    @BeforeEach
    public void set() {
        indicatorsService = new IndicatorsService(mock(IndicatorsRepository.class));
    }

    @Test
    public void filterAndSortIndicatorList_test() {
        assertThatThrownBy(() -> indicatorsService.filterAndSortIndicatorList(null, IndicatorInterval.YEAR))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> indicatorsService.filterAndSortIndicatorList(Collections.emptyList(), null))
            .isInstanceOf(IllegalArgumentException.class);

        List<Indicators> indicatorsList = getStockWithAscendingRoe();
        assertThat(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.YEAR))
            .hasSize(3)
            .allMatch(indicators -> IndicatorType.CONFIRMED.equals(indicators.getIndicatorType()))
            .allMatch(indicators -> IndicatorInterval.YEAR.equals(indicators.getIssuanceCycle()))
            .isSortedAccordingTo(Comparator.comparing(Indicators::getBusinessYear).thenComparing((Indicators::getBusinessMonth)));
        assertThat(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.QUARTER))
            .hasSize(3)
            .allMatch(indicators -> IndicatorType.CONFIRMED.equals(indicators.getIndicatorType()))
            .allMatch(indicators -> IndicatorInterval.QUARTER.equals(indicators.getIssuanceCycle()))
            .isSortedAccordingTo(Comparator.comparing(Indicators::getBusinessYear).thenComparing((Indicators::getBusinessMonth)));
    }

    ///////////////////////////////////////////////
    //               getRoeTrend test            //
    ///////////////////////////////////////////////
    @Test
    public void roe_trend_is_ASCENDING_test() {
        List<Indicators> indicatorsList = getStockWithAscendingRoe();

        assertThat(indicatorsService.getRoeTrend(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.QUARTER))).isEqualTo(Trend.ASCENDING);
        assertThat(indicatorsService.getRoeTrend(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.YEAR))).isEqualTo(Trend.ASCENDING);
    }

    @Test
    public void roe_trend_is_DESCENDING_test() {
        List<Indicators> indicatorsList = getStockWithDescendingRoe();

        assertThat(indicatorsService.getRoeTrend(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.QUARTER))).isEqualTo(Trend.DESCENDING);
        assertThat(indicatorsService.getRoeTrend(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.YEAR))).isEqualTo(Trend.DESCENDING);
    }

    @Test
    public void roe_trend_is_NO_TREND_test() {
        List<Indicators> indicatorsList = getStockWithNoTrendRoe();

        assertThat(indicatorsService.getRoeTrend(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.YEAR))).isEqualTo(Trend.NO_TREND);
        assertThat(indicatorsService.getRoeTrend(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.QUARTER))).isEqualTo(Trend.NO_TREND);
    }

    @Test
    public void roe_trend_is_NO_TREND_when_indicators_is_less_then_3_test() {
        List<Indicators> indicatorsList = getStockWithLessThan3Roe();

        assertThat(indicatorsService.getRoeTrend(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.YEAR))).isEqualTo(Trend.NO_TREND);
        assertThat(indicatorsService.getRoeTrend(indicatorsService.filterAndSortIndicatorList(indicatorsList, IndicatorInterval.QUARTER))).isEqualTo(Trend.NO_TREND);
    }

    @Test
    public void throw_exception_when_param_is_null_test() {
        assertThatThrownBy(() -> indicatorsService.getRoeTrend(null))
            .isInstanceOf(IllegalArgumentException.class);
    }


    private List<Indicators> getStockWithAscendingRoe() {
        return List.of(
            getConfirmedIndicators(2019, 12, IndicatorInterval.YEAR, BigDecimal.ZERO),
            getConfirmedIndicators(2020, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(1)),
            getConfirmedIndicators(2021, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(2)),
            getExpectedIndicators(2021, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(2)),
            getConfirmedIndicators(2021, 6, IndicatorInterval.QUARTER, BigDecimal.ZERO),
            getConfirmedIndicators(2021, 9, IndicatorInterval.QUARTER, BigDecimal.valueOf(1)),
            getConfirmedIndicators(2021, 12, IndicatorInterval.QUARTER, BigDecimal.valueOf(2)),
            getExpectedIndicators(2021, 12, IndicatorInterval.QUARTER, BigDecimal.valueOf(2))
        );
    }

    private List<Indicators> getStockWithDescendingRoe() {
        return List.of(
            getConfirmedIndicators(2019, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(2)),
            getConfirmedIndicators(2020, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(1)),
            getConfirmedIndicators(2021, 12, IndicatorInterval.YEAR, BigDecimal.ZERO),
            getConfirmedIndicators(2021, 6, IndicatorInterval.QUARTER, BigDecimal.valueOf(2)),
            getConfirmedIndicators(2021, 9, IndicatorInterval.QUARTER, BigDecimal.valueOf(1)),
            getConfirmedIndicators(2021, 12, IndicatorInterval.QUARTER, BigDecimal.ZERO)
        );
    }

    private List<Indicators> getStockWithNoTrendRoe() {
        return List.of(
            getConfirmedIndicators(2019, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(1)),
            getConfirmedIndicators(2020, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(2)),
            getConfirmedIndicators(2021, 12, IndicatorInterval.YEAR, BigDecimal.ZERO),
            getConfirmedIndicators(2021, 6, IndicatorInterval.QUARTER, BigDecimal.valueOf(1)),
            getConfirmedIndicators(2021, 9, IndicatorInterval.QUARTER, BigDecimal.valueOf(2)),
            getConfirmedIndicators(2021, 12, IndicatorInterval.QUARTER, BigDecimal.ZERO)
        );
    }

    private List<Indicators> getStockWithLessThan3Roe() {
        return List.of(
            getConfirmedIndicators(2019, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(1)),
            getConfirmedIndicators(2020, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(2)),
            getExpectedIndicators(2021, 12, IndicatorInterval.YEAR, BigDecimal.valueOf(3)),
            getConfirmedIndicators(2021, 6, IndicatorInterval.QUARTER, BigDecimal.valueOf(1)),
            getConfirmedIndicators(2021, 9, IndicatorInterval.QUARTER, BigDecimal.valueOf(2))
        );
    }

    private Indicators getConfirmedIndicators(Integer businessYear, Integer businessMonth, IndicatorInterval indicatorInterval, BigDecimal roe) {
        return ConfirmedIndicators.from(
            IndicatorsParam.builder()
                .issuanceCycle(indicatorInterval)
                .businessYear(businessYear)
                .businessMonth(businessMonth)
                .roe(roe)
                .build()
        );
    }

    private Indicators getExpectedIndicators(Integer businessYear, Integer businessMonth, IndicatorInterval indicatorInterval, BigDecimal roe) {
        return ExpectedIndicators.from(
            IndicatorsParam.builder()
                .issuanceCycle(indicatorInterval)
                .businessYear(businessYear)
                .businessMonth(businessMonth)
                .roe(roe)
                .build()
        );
    }

    ///////////////////////////////////////////////
    //            getYieldGrowth test            //
    ///////////////////////////////////////////////
    @Test
    public void geYieldGrowth_test() {
        assertThatThrownBy(() -> indicatorsService.getYieldGrowth(null))
            .isInstanceOf(IllegalArgumentException.class).describedAs("파라미터 null 체크");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.ONE),
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.ZERO)
            ))
        ).isEqualTo(Trend.NO_TREND).describedAs("indicatorsList.size < 3 일 경우 no trend");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.ZERO),
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.ONE),
            getConfirmedIndicators(2021, null, IndicatorInterval.YEAR, BigDecimal.ZERO)
            ))
        ).isEqualTo(Trend.DESCENDING).describedAs("상승 - 하강 인 경우 DESCENDING");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.ONE),
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.ZERO),
            getConfirmedIndicators(2021, null, IndicatorInterval.YEAR, BigDecimal.ONE)
            ))
        ).isEqualTo(Trend.ASCENDING).describedAs("하강 - 상승 인 경우 ASCENDING");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.ZERO),
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.ONE),
            getConfirmedIndicators(2021, null, IndicatorInterval.YEAR, BigDecimal.TEN)
            ))
        ).isEqualTo(Trend.ASCENDING).describedAs("증가량이 커지는 경우 ASCENDING");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.ZERO),
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.TEN),
            getConfirmedIndicators(2021, null, IndicatorInterval.YEAR, BigDecimal.valueOf(11))
            ))
        ).isEqualTo(Trend.DESCENDING).describedAs("증가량이 작아지는 경우 DESCENDING");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.ZERO),
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.TEN),
            getConfirmedIndicators(2021, null, IndicatorInterval.YEAR, BigDecimal.valueOf(20))
            ))
        ).isEqualTo(Trend.NO_TREND).describedAs("증가량이 일정한 경우 NO_TREND");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.valueOf(11)),
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.TEN),
            getConfirmedIndicators(2021, null, IndicatorInterval.YEAR, BigDecimal.ZERO)
            ))
        ).isEqualTo(Trend.DESCENDING).describedAs("감소량이 커지는 경우 DESCENDING");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.TEN),
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.ONE),
            getConfirmedIndicators(2021, null, IndicatorInterval.YEAR, BigDecimal.ZERO)
            ))
        ).isEqualTo(Trend.ASCENDING).describedAs("감소량이 작아지는 경우 ASCENDING");
        assertThat(indicatorsService.getYieldGrowth(List.of(
            getConfirmedIndicators(2019, null, IndicatorInterval.YEAR, BigDecimal.valueOf(20)),
            getConfirmedIndicators(2020, null, IndicatorInterval.YEAR, BigDecimal.TEN),
            getConfirmedIndicators(2021, null, IndicatorInterval.YEAR, BigDecimal.ZERO)
            ))
        ).isEqualTo(Trend.NO_TREND).describedAs("감소량이 일정한 경우 NO_TREND");
    }
}
