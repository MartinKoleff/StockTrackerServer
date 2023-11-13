package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Timezone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimezoneRepository extends JpaRepository<Timezone, Long> {
    Optional<Timezone> findByStockExchanges_Stocks_Tag(String tag);
    Optional<Timezone> findTimezoneByTimezone(String timezone);
    void deleteByTimezone(String timezone);
}
