package com.jh.stock.domain.repository;

import com.jh.stock.domain.MyQuote;

import java.util.Collection;
import java.util.List;

public interface MyQuoteRepositoryCustom {
    List<MyQuote> getLatestQuoteBy(Collection<String> tickers);
}
