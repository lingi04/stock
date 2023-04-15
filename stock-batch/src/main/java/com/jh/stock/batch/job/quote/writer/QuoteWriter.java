package com.jh.stock.batch.job.quote.writer;

import com.jh.stock.domain.MyQuote;
import com.jh.stock.domain.service.MyQuoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class QuoteWriter implements ItemWriter<MyQuote> {
    private final MyQuoteService myQuoteService;

    @Override
    public void write(List<? extends MyQuote> quoteList) throws Exception {
        quoteList.stream().filter(Objects::nonNull).forEach(quote -> {
            myQuoteService.save(quote);
            log.info("티커 : {}, 시장가 : {}, 등락폭 : {}, 거래량 : {}", quote.getTicker(), quote.getMarketPrice(), quote.getFluctuation(), quote.getTransactionVolume());
        });
    }
}
