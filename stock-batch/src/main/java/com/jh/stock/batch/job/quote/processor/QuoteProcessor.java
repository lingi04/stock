package com.jh.stock.batch.job.quote.processor;

import com.jh.stock.domain.MyQuote;
import com.jh.stock.domain.MyStock;
import com.jh.stock.domain.StockMarketHolidays;
import com.jh.stock.domain.dto.MyQuoteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

import java.sql.Timestamp;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuoteProcessor implements ItemProcessor<MyStock, MyQuote> {
    @Override
    public MyQuote process(MyStock item) throws Exception {
        if (StockMarketHolidays.isHolidayToday()) {
            return null;
        }
        return getQuoteFromYahooFinancial(item.getTicker());
    }

    private MyQuote getQuoteFromYahooFinancial(String ticker) {
        try {
            Stock stock = YahooFinance.get(ticker + ".ks");
            StockQuote stockQuote = stock.getQuote();

            return MyQuote.of(MyQuoteDto.builder()
                .ticker(ticker)
                .tradeDate(Timestamp.from(stockQuote.getLastTradeTime().toInstant()))
                .marketPrice(stockQuote.getPrice())
                .fluctuation(stockQuote.getChange())
                .marketCapitalization(stock.getStats().getMarketCap())
                .yearHigh(stockQuote.getYearHigh())
                .yearLow(stockQuote.getYearLow())
                .transactionVolume(stockQuote.getVolume())
                .build());
        } catch (Exception e) {
            log.error("ticker : {}, reason : {}", ticker, e.getMessage());
        }

        return null;
    }

}
