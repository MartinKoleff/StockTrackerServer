package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findStockByTag(String stockTag);
    List<Stock> findByHasIntraDay(Boolean hasIntraDay);
    List<Stock> findByHasEndOfDay(Boolean hasEndOfDay);
    int deleteStockById(Long id);
}
