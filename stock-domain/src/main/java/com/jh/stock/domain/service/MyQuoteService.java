package com.jh.stock.domain.service;

import com.jh.stock.domain.MyQuote;
import com.jh.stock.domain.repository.MyQuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyQuoteService {
    private final MyQuoteRepository myQuoteRepository;

    public void save(MyQuote myQuote) {
        myQuoteRepository.save(myQuote);
    }

    public Map<String, MyQuote> getLatestQuoteByTicker(Collection<String> tickerSet) {
        return myQuoteRepository.getLatestQuoteBy(tickerSet).stream()
            .collect(Collectors.toMap(MyQuote::getTicker, Function.identity()));

    }
}
