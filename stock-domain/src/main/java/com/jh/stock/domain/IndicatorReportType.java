package com.jh.stock.domain;

import com.jh.stock.domain.common.TriFunction;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.BiFunction;

public enum IndicatorReportType {
    ROE("ROE",
        (Indicators after, Indicators before) -> {
            BigDecimal afterRoe = Optional.ofNullable(after.getRoe()).orElse(BigDecimal.ZERO);
            BigDecimal beforeRoe = Optional.ofNullable(before.getRoe()).orElse(BigDecimal.ZERO);

            return afterRoe.compareTo(beforeRoe);
        },
        (Indicators after, Indicators before, Optional<Indicators> expected) -> {
            String rtnStr = before.getRoe() + "-> " + after.getRoe();
            if (expected.isPresent()) {
                rtnStr += " -> " + expected.get().getRoe();
            }
            return rtnStr;
        }
    ),
    TOTAL_REVENUE("매출액",
        (Indicators after, Indicators before) -> {
            Long a = Optional.ofNullable(after.getTotalRevenue()).orElse(0l);
            Long b = Optional.ofNullable(before.getTotalRevenue()).orElse(0l);

            return a.compareTo(b);
        },
        (Indicators after, Indicators before, Optional<Indicators> expected) -> {
            String rtnStr = before.getTotalRevenue() + " -> " + after.getTotalRevenue();
            if (expected.isPresent()) {
                rtnStr += " -> " + expected.get().getTotalRevenue();
            }
            return rtnStr;
        }
    ),
    TOTAL_EQUITY("자본 총계",
        (Indicators after, Indicators before) -> {
            Long a = Optional.ofNullable(after.getTotalEquity()).orElse(0l);
            Long b = Optional.ofNullable(before.getTotalEquity()).orElse(0l);

            return a.compareTo(b);
        },
        (Indicators after, Indicators before, Optional<Indicators> expected) -> {
            String rtnStr = before.getTotalEquity() + " -> " + after.getTotalEquity();
            if (expected.isPresent()) {
                rtnStr += " -> " + expected.get().getTotalEquity();
            }
            return rtnStr;
        }
    ),
    OPERATING_PROFIT("영업이익",
        (Indicators after, Indicators before) -> {
            Long a = Optional.ofNullable(after.getOperatingProfit()).orElse(0l);
            Long b = Optional.ofNullable(before.getOperatingProfit()).orElse(0l);

            return a.compareTo(b);
        },
        (Indicators after, Indicators before, Optional<Indicators> expected) -> {
            String rtnStr = before.getOperatingProfit() + " -> " + after.getOperatingProfit();
            if (expected.isPresent()) {
                rtnStr += " -> " + expected.get().getOperatingProfit();
            }
            return rtnStr;
        }
    ),
    TOTAL_LIABILITIES("부채 총계",
        (Indicators after, Indicators before) -> {
            Long a = Optional.ofNullable(after.getTotalLiabilities()).orElse(0l);
            Long b = Optional.ofNullable(before.getTotalLiabilities()).orElse(0l);

            return a.compareTo(b);
        },
        (Indicators after, Indicators before, Optional<Indicators> expected) -> {
            String rtnStr = before.getTotalLiabilities() + " -> " + after.getTotalLiabilities();
            if (expected.isPresent()) {
                rtnStr += " -> " + expected.get().getTotalLiabilities();
            }
            return rtnStr;
        }
    );

    String describe;
    BiFunction<Indicators, Indicators, Integer> comparator;
    TriFunction<Indicators, Indicators, Optional<Indicators>, String> reportContent;

    public String getContent(Indicators after, Indicators before, Optional<Indicators> expected) {
        return reportContent.apply(after, before, expected);
    }

    public String getDescribe() {
        return this.describe;
    }

    IndicatorReportType(String describe,
                        BiFunction<Indicators, Indicators, Integer> comparator,
                        TriFunction<Indicators, Indicators, Optional<Indicators>, String> reportContent) {
        this.describe = describe;
        this.comparator = comparator;
        this.reportContent = reportContent;
    }
}
