package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByStockExchanges_Stocks_Tag(String tag);

    Optional<Currency> findByCode(String code);

    void deleteByCode(String code);
}
