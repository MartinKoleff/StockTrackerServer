package com.koleff.stockserver.stocks.repository.impl;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.repository.custom.RepositoryCustom;
import com.koleff.stockserver.stocks.repository.custom.StockRepositoryCustom;
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
public interface StockRepositoryImpl extends StockRepository, RepositoryCustom, StockRepositoryCustom {

    @Override
    @Query(value = "SELECT s FROM Stock s WHERE s.tag = ?1")
    Optional<Stock> findStockByTag(String stockTag);

    @Override
    @Query(value = "SELECT s FROM Stock s WHERE s.hasIntraDay = ?1")
    List<Stock> findByHasIntraDay(Boolean hasIntraDay);

    @Override
    @Query(value = "SELECT s FROM Stock s WHERE s.hasEndOfDay = ?1")
    List<Stock> findByHasEndOfDay(Boolean hasEndOfDay);

    @Override
    @Modifying
    @Query("UPDATE Stock s SET s.hasIntraDay = true WHERE s.intraDay IS NOT NULL")
    void updateIntraDayStatus();

    @Override
    @Modifying
    @Query("UPDATE Stock s SET s.hasEndOfDay = true WHERE s.endOfDay IS NOT NULL")
    void updateEndOfDayStatus();

    @Override
    @Modifying
    @Query("DELETE FROM Stock s WHERE s.id = ?1")
    int deleteStockById(Long id);

    @Override
    @Modifying
    @Query("DELETE FROM Stock s WHERE s.tag = ?1")
    int deleteByStockTag(String stockTag);

    @Override
    @Modifying
    @Query(value = "TRUNCATE TABLE stock RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncate();
}