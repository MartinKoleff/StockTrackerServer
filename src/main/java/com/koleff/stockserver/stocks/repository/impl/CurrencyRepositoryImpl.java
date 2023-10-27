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
            value = "SELECT c FROM Currency c " +
                    "JOIN StockExchange se ON (se.currencyId = c.id)" +
                    "JOIN Stock s ON (s.stockExchangeId = se.id)" +
                    "WHERE s.tag = ?1"
    )
    Optional<Currency> findByStockTag(String stockTag);

    @Query(
            value = "SELECT c.code FROM Currency c "
    )
    Optional<List<String>> getCurrencyCodes();

    @Override
    @Query(
            value = "SELECT c FROM Currency c " +
                    "WHERE c.code = ?1"
    )
    Optional<Currency> findCurrencyByCurrencyCode(String currencyCode);

    @Override
    @Modifying
    @Query("DELETE FROM Currency c WHERE c.code = ?1")
    void deleteByCurrencyCode(String currencyCode);
}

