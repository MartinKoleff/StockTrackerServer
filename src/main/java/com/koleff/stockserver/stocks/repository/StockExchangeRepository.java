package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {
    List<StockExchange> findByCountry(String country);
    void truncateStockExchange();
}
