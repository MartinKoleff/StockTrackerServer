package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByStockTag(String stockTag);

    Optional<List<String>> getCurrencyCodes();

    Optional<Currency> findCurrencyByCurrencyCode(String currencyCode);

    void deleteByCurrencyCode(String currencyCode);
}
