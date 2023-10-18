package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true,
               rollbackFor = Exception.class,
               propagation = Propagation.REQUIRED
)
public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {

    @Query(
            value = "SELECT * FROM stockExchange se WHERE se.country = ?1",
            nativeQuery = true)
    List<StockExchange> getStockExchangeByCountry(String country);
}
