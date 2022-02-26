package com.jh.stock.domain.repository;


import com.jh.stock.domain.IndicatorType;
import com.jh.stock.domain.Indicators;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IndicatorsRepository extends CrudRepository<Indicators, Long> {
    List<Indicators> findAllByTickerAndBusinessYearInAndIndicatorType(String ticker, List<Integer> businessYearList, IndicatorType indicatorType);

    List<Indicators> findAllByTickerIn(Collection<String> tickerList);
}
