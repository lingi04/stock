package com.jh.stock.domain;

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
        (Indicators after, Indicators before) -> before.getRoe() + "-> " + after.getRoe()
    ),
    TOTAL_REVENUE("매출액",
        (Indicators after, Indicators before) -> {
            Long a = Optional.ofNullable(after.getTotalRevenue()).orElse(0l);
            Long b = Optional.ofNullable(before.getTotalRevenue()).orElse(0l);

            return a.compareTo(b);
        },
        (Indicators after, Indicators before) -> before.getTotalRevenue() + " -> " + after.getTotalRevenue()
    ),
    TOTAL_EQUITY("자본 총계",
        (Indicators after, Indicators before) -> {
            Long a = Optional.ofNullable(after.getTotalEquity()).orElse(0l);
            Long b = Optional.ofNullable(before.getTotalEquity()).orElse(0l);

            return a.compareTo(b);
        },
        (Indicators after, Indicators before) -> before.getTotalEquity() + " -> " + after.getTotalEquity()
    ),
    OPERATING_PROFIT("영업이익",
        (Indicators after, Indicators before) -> {
            Long a = Optional.ofNullable(after.getOperatingProfit()).orElse(0l);
            Long b = Optional.ofNullable(before.getOperatingProfit()).orElse(0l);

            return a.compareTo(b);
        },
        (Indicators after, Indicators before) -> before.getOperatingProfit() + " -> " + after.getOperatingProfit()
    ),
    TOTAL_LIABILITIES("부채 총계",
        (Indicators after, Indicators before) -> {
            Long a = Optional.ofNullable(after.getTotalLiabilities()).orElse(0l);
            Long b = Optional.ofNullable(before.getTotalLiabilities()).orElse(0l);

            return a.compareTo(b);
        },
        (Indicators after, Indicators before) -> before.getTotalLiabilities() + " -> " + after.getTotalLiabilities()
    );

    String describe;
    BiFunction<Indicators, Indicators, Integer> comparator;
    BiFunction<Indicators, Indicators, String> reportContent;

    public String getContent(Indicators after, Indicators before) {
        return reportContent.apply(after, before);
    }

    public String getDescribe() {
        return this.describe;
    }

    IndicatorReportType(String describe,
                        BiFunction<Indicators, Indicators, Integer> comparator,
                        BiFunction<Indicators, Indicators, String> reportContent) {
        this.describe = describe;
        this.comparator = comparator;
        this.reportContent = reportContent;
    }
}
