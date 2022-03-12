package com.jh.stock.domain.service;

import com.jh.stock.domain.IndicatorInterval;
import com.jh.stock.domain.IndicatorType;
import com.jh.stock.domain.Indicators;
import com.jh.stock.domain.Trend;
import com.jh.stock.domain.repository.IndicatorsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndicatorsService {
    private final IndicatorsRepository indicatorsRepository;

    public void save(Indicators indicators) {
        indicatorsRepository.save(indicators);
    }

    public List<Indicators> findAllByTickerAndBusinessYearInAndType(String ticker, List<Integer> list, IndicatorType indicatorType) {
        return indicatorsRepository.findAllByTickerAndBusinessYearInAndIndicatorType(ticker, list, indicatorType);
    }

    public List<Indicators> filterAndSortIndicatorList(@NonNull List<Indicators> indicatorsList, @NonNull IndicatorInterval indicatorInterval) {
        List<Indicators> tmpList = Optional.ofNullable(indicatorsList).orElse(Collections.emptyList());

        return tmpList.stream()
            .filter(indicators -> indicators.getIssuanceCycle() == indicatorInterval)
            .filter(Indicators::isConfirmed)
            .sorted(Comparator.comparing(Indicators::getBusinessYear).thenComparing((Indicators::getBusinessMonth)))
            .limit(3)
            .collect(Collectors.toList());
    }

    /**
     * 예상 roe
     *
     * @param roeList
     * @param yieldTrend
     * @return
     */
    public BigDecimal calculateExpectedRoe(List<BigDecimal> roeList, Trend yieldTrend) {
        if (Objects.isNull(roeList)) {
            return BigDecimal.ZERO;
        }
        if (roeList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        if (yieldTrend == Trend.ASCENDING) {
            return new BigDecimal(roeList.get(roeList.size() - 1).toString());
        } else if (yieldTrend == Trend.DESCENDING) {
            return new BigDecimal(roeList.get(roeList.size() - 1).toString());
        } else {
            BigDecimal sum = IntStream.range(0, roeList.size())
                .mapToObj(i -> roeList.get(i).multiply(new BigDecimal(i + 1)))
                .reduce(new BigDecimal(0), BigDecimal::add);
            int size = roeList.size();
            int div = (size * (size + 1)) / 2;

            log.info("[SRimService][calculateExpectedRoe] sum : {}, div : {}", sum, div);
            return sum.divide(new BigDecimal(div), 2, RoundingMode.DOWN);
        }
    }

    /**
     * filteredIndicatorList 를 파라미터로 입력받아 roe trend 를 반환한다.
     *
     * @param filteredIndicatorList
     * @return
     */
    public Trend getRoeTrend(@NonNull List<Indicators> filteredIndicatorList) {
        if (filteredIndicatorList.size() < 3) {
            log.info("not enough data");

            return Trend.NO_TREND;
        }

        List<BigDecimal> roeDiff = new ArrayList<>();

        for (int i = 1; i < filteredIndicatorList.size(); i++) {
            roeDiff.add(filteredIndicatorList.get(i).getRoe()
                .subtract(filteredIndicatorList.get(i - 1).getRoe())
            );
        }

        if (roeDiff.stream().allMatch(this::isPositive)) {
            return Trend.ASCENDING;
        }

        if (roeDiff.stream().allMatch(this::isNegative)) {
            return Trend.DESCENDING;
        }

        return Trend.NO_TREND;
    }

    /**
     * @param filteredIndicatorList
     * @return
     */
    public Trend getYieldGrowth(@NonNull List<Indicators> filteredIndicatorList) {
        if (filteredIndicatorList.size() < 3) return Trend.NO_TREND;

        List<BigDecimal> roeList = filteredIndicatorList.stream().map(Indicators::getRoe).collect(Collectors.toList());
        // d = roe_0 + roe_2 - 2 * (roe_1)
        BigDecimal d = roeList.get(0).add(roeList.get(2)).subtract(roeList.get(1).multiply(BigDecimal.valueOf(2)));

        if (isPositive(d)) {
            return Trend.ASCENDING;
        } else if (isNegative(d)) {
            return Trend.DESCENDING;
        } else {
            return Trend.NO_TREND;
        }
    }

    private boolean isPositive(BigDecimal bigDecimal) {
        if (Objects.nonNull(bigDecimal)) {
            return bigDecimal.compareTo(BigDecimal.ZERO) > 0;
        }

        return false;
    }

    private boolean isNegative(BigDecimal bigDecimal) {
        if (Objects.nonNull(bigDecimal)) {
            return bigDecimal.compareTo(BigDecimal.ZERO) < 0;
        }

        return false;
    }

    /**
     * 지배주주지분
     *
     * @param indicatorsList
     * @return
     */
    public Long getLatestShareOfControllingShareholder(@NonNull List<Indicators> indicatorsList) {
        return indicatorsList.stream()
            .sorted(Comparator.comparing(Indicators::getBusinessYear).thenComparing((Indicators::getBusinessMonth)))
            .collect(Collectors.toList())
            .get(indicatorsList.size() - 1).getShareOfControllingShareholder();
    }

    /**
     * 발행 주식 수
     *
     * @param indicatorsList
     * @return
     */
    public Long getLatestNumberOfIssuedShares(@NonNull List<Indicators> indicatorsList) {
        return indicatorsList.stream()
            .sorted(Comparator.comparing(Indicators::getBusinessYear).thenComparing((Indicators::getBusinessMonth)))
            .collect(Collectors.toList())
            .get(indicatorsList.size() - 1).getNumberOfIssuedShares();
    }

    public int getLatestYear(List<Indicators> indicatorsList) {
        return indicatorsList.stream().map(Indicators::getBusinessYear).max(Integer::compareTo).orElse(0);
    }

    public void deleteBy(String ticker, IndicatorType expected) {
        indicatorsRepository.deleteAllByTickerAndIndicatorType(ticker, expected);
    }

    public List<Indicators> dateDesc(List<Indicators> indicatorsList) {
        List<Indicators> tmpList = Optional.ofNullable(indicatorsList).orElse(Collections.emptyList());

        return tmpList.stream()
            .sorted(Comparator.comparingInt(Indicators::getBusinessYear).thenComparingInt((Indicators::getBusinessMonth)).reversed())
            .collect(Collectors.toList());
    }

    public Optional<Indicators> getExpectedLatestIndicators(List<Indicators> indicatorsList) {
        return indicatorsList.stream().max(Comparator.comparing(Indicators::getBusinessYear).thenComparing(Indicators::getBusinessMonth));
    }
}
