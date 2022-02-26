package com.jh.stock.domain.repository;

import com.jh.stock.domain.MyQuote;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

import static com.jh.stock.domain.QMyQuote.myQuote;

@RequiredArgsConstructor
public class MyQuoteRepositoryImpl implements MyQuoteRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MyQuote> getLatestQuoteBy(@NonNull Collection<String> tickers) {
        return queryFactory.selectFrom(myQuote)
            .where(
                Expressions.list(myQuote.ticker, myQuote.tradeDate).in(
                    queryFactory
                        .select(myQuote.ticker, myQuote.tradeDate.max())
                        .from(myQuote)
                        .where(myQuote.ticker.in(tickers))
                        .groupBy(myQuote.ticker)
                ))
            .fetch();
    }
}
