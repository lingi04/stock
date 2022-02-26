package com.jh.stock.domain.repository;

import com.jh.stock.domain.Favorites;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepository extends CrudRepository<Favorites, Long> {
}
