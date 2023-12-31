package com.koleff.stockserver.stocks.repository.impl;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.repository.EndOfDayRepository;
import com.koleff.stockserver.stocks.repository.custom.EndOfDayRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(
        rollbackFor = Exception.class,
        propagation = Propagation.REQUIRED
)
public interface EndOfDayRepositoryImpl extends EndOfDayRepository, EndOfDayRepositoryCustom {
    @Override
    @Query(
            value = "SELECT eod FROM EndOfDay eod " +
                    "JOIN Stock s ON (s.id = eod.stockId) " +
                    "WHERE s.tag = ?1"
    )
    Optional<List<EndOfDay>> findEndOfDayByStockTag(String stockTag);

    @Override
    @Query(
            value = "SELECT eod FROM EndOfDay eod " +
                    "JOIN Stock s ON (s.id = eod.stockId) " +
                    "WHERE s.tag = ?1 AND " +
                    "eod.date BETWEEN ?2 AND ?3"
    )
    Optional<List<EndOfDay>> findEndOfDayByStockTagAndDateBetween(String stockTag, String dateFrom, String dateTo);

    @Override
    @Query(
            value = "SELECT eod FROM EndOfDay eod " +
                    "JOIN Stock s ON (s.id = eod.stockId) " +
                    "WHERE s.tag = ?1 AND " +
                    "eod.date = ?2"
    )
    Optional<List<EndOfDay>> findEndOfDayByStockTagAndDate(String stockTag, String date);

    @Override
    @Query(
            value = "SELECT eod FROM EndOfDay eod " +
                    "WHERE eod.id = ?1"
    )
    Optional<List<EndOfDay>> findAllById(Long stockId);

    @Override
    @Modifying
    @Query(
            value = "DELETE FROM end_of_day eod " +
                    "USING stock s " +
                    "WHERE s.tag = $1",
            nativeQuery = true
    )
    int deleteByTag(String stockTag);

    @Override
    @Modifying
    @Query(value = "TRUNCATE TABLE end_of_day RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncate();
}

