package com.koleff.stockserver.stocks.repository.impl;

import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.repository.TimezoneRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional(
        rollbackFor = Exception.class,
        propagation = Propagation.REQUIRED
)
public interface TimezoneRepositoryImpl extends TimezoneRepository {

    @Override
    @Query(
            value = "SELECT t FROM Timezone t, StockExchange se, Stock s " +
                    "Where s.tag = ?1"
    )
    Optional<Timezone> findByStockExchanges_Stocks_Tag(String stockTag);

    @Override
    @Query(
            value = "SELECT t FROM Timezone t " +
                    "WHERE t.timezone = ?1"
    )
    Optional<Timezone> findTimezoneByTimezone(String timezone);

    @Override
    @Modifying
    @Query("DELETE FROM Timezone t WHERE t.timezone = ?1")
    void deleteByTimezone(String timezone);


    @Override
    @Modifying
    @Query(value = "TRUNCATE TABLE timezone RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateTimezone();
}

