package com.koleff.stockserver.stocks.repository.impl;

import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.repository.CurrencyRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional(readOnly = true,
        rollbackFor = Exception.class,
        propagation = Propagation.REQUIRED
)
public interface CurrencyRepositoryImpl extends CurrencyRepository {

    @Query(
            value = "SELECT * FROM currency c " +
                    "JOIN stock_exchange se ON (se.currency_id = c.id)" +
                    "JOIN stock s ON (s.stock_exchange_id = se.id)" +
                    "WHERE s.tag = ?1",
            nativeQuery = true
    )
    Optional<Currency> findByStockTag(String stockTag);

    @Query(
            value = "SELECT code FROM currency c ",
            nativeQuery = true
    )
    Optional<List<String>> getCurrencyCodes();

    @Override
    @Query(
            value = "SELECT * FROM currency c " +
                    "WHERE c.code = ?1",
            nativeQuery = true
    )
    Optional<Currency> findCurrencyByCurrencyCode(String currencyCode);

    @Override
    @Modifying
    @Query("DELETE FROM Currency c WHERE c.code = ?1")
    void deleteByCurrencyCode(String currencyCode);
}

