package com.jh.stock.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class StockMarketHolidaysTest {

    @Test
    void isHolidayToday_return_false_test() {
        LocalDate expected = LocalDate.of(2023, 4, 3);
        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class);
             MockedStatic<ZoneId> mockedZoneId = mockStatic(ZoneId.class)
        ) {
            when(ZoneId.of("Asia/Seoul")).thenReturn(zoneId);
            when(LocalDate.now(any(ZoneId.class))).thenReturn(expected);
            assertThat(StockMarketHolidays.isHolidayToday()).isFalse();
        }
    }

    @Test
    void isHolidayToday_return_true_test() {
        LocalDate expected = LocalDate.of(2023, 12, 25);
        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class);
             MockedStatic<ZoneId> mockedZoneId = mockStatic(ZoneId.class)
        ) {
            when(ZoneId.of("Asia/Seoul")).thenReturn(zoneId);
            when(LocalDate.now(any(ZoneId.class))).thenReturn(expected);
            assertThat(StockMarketHolidays.isHolidayToday()).isTrue();
        }
    }
}
