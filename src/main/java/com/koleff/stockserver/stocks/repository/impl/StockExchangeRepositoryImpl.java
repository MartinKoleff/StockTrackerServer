package com.koleff.stockserver.stocks.repository.impl;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.repository.StockExchangeRepository;
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
public interface StockExchangeRepositoryImpl extends StockExchangeRepository {

    @Override
    @Query(value = "SELECT se, " +
            "c.id AS currency_id_fk, c.code, c.name AS currency_name, c.symbol, " +
            "t.id AS timezone_id_fk, t.abbreviation, t.abbreviationDst, t.timezone " +
            "FROM StockExchange se " +
            "JOIN Currency c ON c.id = se.currencyId " +
            "JOIN Timezone t ON t.id = se.timezoneId"
    )
    List<StockExchange> findAll();

    @Override
    @Query(
            value = "SELECT se FROM StockExchange se WHERE se.country = ?1"
    )
    List<StockExchange> findByCountry(String country);

    @Override
    @Modifying
    @Query(value = "TRUNCATE TABLE stock_exchange RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncate();
}
