package com.jh.stock.domain.repository;

import com.jh.stock.domain.MyStock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MyStockRepository extends CrudRepository<MyStock, Integer> {
    List<MyStock> findMyStockByTickerIn(Set<String> tickerSet);
}
