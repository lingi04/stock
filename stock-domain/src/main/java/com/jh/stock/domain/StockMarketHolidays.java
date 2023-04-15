package com.jh.stock.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

public class StockMarketHolidays {
    private static final Set<String> HOLIDAY_KR;

    static {
        String[] stockMarketHolidays = {
            "2023-01-23", "2023-01-24", "2023-03-01", "2023-05-01", "2023-05-05", "2023-06-06", "2023-08-15",
            "2023-09-28", "2023-09-29", "2023-10-03", "2023-10-09", "2023-12-25", "2023-12-29", "2024-01-01",
            "2024-02-09", "2024-02-12", "2024-03-01", "2024-05-01", "2024-05-06", "2024-05-15", "2024-06-06",
            "2024-08-15", "2024-09-16", "2024-09-17", "2024-09-18", "2024-10-03", "2024-10-09", "2024-12-25",
            "2024-12-31", "2025-01-01", "2025-01-28", "2025-01-29", "2025-01-30", "2025-03-03", "2025-05-01",
            "2025-05-05", "2025-05-06", "2025-06-06", "2025-08-15", "2025-10-03", "2025-10-06", "2025-10-07",
            "2025-10-08", "2025-10-09", "2025-12-25", "2025-12-31", "2026-01-01", "2026-02-16", "2026-02-17",
            "2026-02-18", "2026-03-02", "2026-05-01", "2026-05-05", "2026-08-17", "2026-09-24", "2026-09-25",
            "2026-10-05", "2026-10-09", "2026-12-25", "2026-12-31", "2027-01-01", "2027-02-08", "2027-02-09",
            "2027-03-01", "2027-05-05"};

        HOLIDAY_KR = new HashSet<>(Arrays.asList(stockMarketHolidays));
    }

    public static boolean isHolidayToday() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek == SUNDAY || dayOfWeek == SATURDAY) {
            return true;
        }

        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return HOLIDAY_KR.contains(todayStr);
    }
}
