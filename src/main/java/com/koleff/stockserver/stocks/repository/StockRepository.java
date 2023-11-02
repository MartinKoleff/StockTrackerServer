package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findStockByTag(String stockTag);
    List<Stock> findByHasIntraDayIsTrue(Boolean hasIntraDay);
    List<Stock> findByHasEndOfDayIsTrue(Boolean hasEndOfDay);
    void updateIntraDayStatus();
    void updateEndOfDayStatus();
    int deleteStockById(Long id);
    int deleteByStockTag(String stockTag);
    void truncateStock();
}
