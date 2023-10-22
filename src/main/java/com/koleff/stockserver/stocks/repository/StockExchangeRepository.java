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

    @Override
    @Query(value = "SELECT se.id, se.currency_id, se.timezone_id, se.acronym, se.city, se.country, se.country_code, " +
            "se.name AS exchange_name, se.website, " +
            "c.id AS currency_id_fk, c.code, c.name, c.symbol, " +
            "t.id AS timezone_id_fk, t.abbreviation, t.abbreviation_dst, t.timezone " +
            "FROM stock_exchange se " +
            "JOIN currency c ON c.id = se.currency_id " +
            "JOIN timezone t ON t.id = se.timezone_id",
            nativeQuery = true
    )
    List<StockExchange> findAll();

    @Query(
            value = "SELECT * FROM stock_exchange se WHERE se.country = ?1",
            nativeQuery = true
    )
    List<StockExchange> getStockExchangeByCountry(String country);
}
