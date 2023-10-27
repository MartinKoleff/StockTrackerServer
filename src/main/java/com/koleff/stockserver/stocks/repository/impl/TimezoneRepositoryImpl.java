package com.koleff.stockserver.stocks.repository.impl;

import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.repository.TimezoneRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional(readOnly = true,
        rollbackFor = Exception.class,
        propagation = Propagation.REQUIRED
)
public interface TimezoneRepositoryImpl extends TimezoneRepository {

    @Override
    @Query(
            value = "SELECT t FROM Timezone t " +
                    "JOIN StockExchange se ON (se.timezoneId = t.id)" +
                    "JOIN Stock s ON (s.stockExchangeId = se.id)" +
                    "WHERE s.tag = ?1"
    )
    Optional<Timezone> findByStockTag(String stockTag);

    @Override
    @Query(
            value = "SELECT t.timezone FROM Timezone t "
    )
    Optional<List<String>> getTimezoneStrings();

    @Override
    @Query(
            value = "SELECT t FROM Timezone t " +
                    "WHERE t.timezone = ?1"
    )
    Collection<Timezone> findTimezoneByTimezoneString(String timezone);

    @Override
    @Modifying
    @Query("DELETE FROM Timezone t WHERE t.timezone = ?1")
    void deleteByTimezone(String timezone);
}

