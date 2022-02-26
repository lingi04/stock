package com.jh.stock.domain.repository;

import com.jh.stock.domain.MyQuote;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

@Primary
public interface MyQuoteRepository extends CrudRepository<MyQuote, Long>, MyQuoteRepositoryCustom {
}
