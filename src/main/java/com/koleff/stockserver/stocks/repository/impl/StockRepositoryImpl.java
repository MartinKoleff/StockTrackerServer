package com.koleff.stockserver.stocks.repository.impl;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.repository.StockRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface StockRepositoryImpl extends StockRepository {

    @Override
    @Query(
            value = "SELECT s.id, s.stock_exchange_id, s.name AS company_name, s.tag, s.has_end_of_day, s.has_intraday, " +
                    "id.id AS intra_day_fk, id.stock_id AS intra_day_stock_id, id.open, id.close, " +
                    "id.high, id.low, id.last, id.volume, id.date, " +
                    "eod.id AS eod_fk, eod.stock_id AS eod_stock_id, eod.open AS eod_open, eod.close AS eod_close, " +
                    "eod.high AS eod_high, eod.low AS eod_low, eod.volume AS eod_volume, eod.adj_open, eod.adj_close, " +
                    "eod.adj_high, eod.adj_low, eod.adj_volume, eod.split_factor, eod.date AS eod_date, " +
                    "se.id AS stock_exchange_fk, se.currency_id, se.timezone_id, se.acronym, se.city, se.country, " +
                    "se.country_code, se.name AS exchange_name, se.website, " +
                    "c.id AS currency_id_fk, c.code, c.name, c.symbol, " +
                    "t.id AS timezone_id_fk, t.abbreviation, t.abbreviation_dst, t.timezone " +
                    "FROM stock s " +
                    "JOIN intra_day id ON id.stock_id = s.id " +
                    "JOIN end_of_day eod ON eod.stock_id = s.id " +
                    "JOIN stock_exchange se ON se.id = s.stock_exchange_id " +
                    "JOIN currency c ON c.id = se.currency_id " +
                    "JOIN timezone t ON t.id = se.timezone_id",
            nativeQuery = true
    )
    List<Stock> findAll();

    @Override
    @Query(value = "SELECT s FROM Stock s WHERE s.tag = ?1")
    Optional<Stock> findStockByStockTag(String stockTag);

    @Override
    @Query(
            value = "SELECT s.tag FROM Stock s"
    )
    Optional<List<String>> getStockTags();

    @Override
    @Query(value = "SELECT s.id FROM Stock s")
    Optional<List<Long>> getStockIds();

    @Override
    @Query(value = "SELECT s FROM Stock s WHERE s.hasIntraDay = ?1")
    List<Stock> selectStocksWhereHasIntraDayEqualTrue(Boolean hasIntraDay);

    @Override
    @Query(value = "SELECT s FROM Stock s WHERE s.hasEndOfDay = ?1")
    List<Stock> selectStocksWhereHasEndOfDayEqualTrue(Boolean hasEndOfDay);

    @Override
    @Modifying
    @Query("UPDATE Stock s SET s.hasIntraDay = true WHERE s.endOfDay != null")
    void updateHasIntraDay();
    
    @Override
    @Modifying
    @Query("UPDATE Stock s SET s.hasEndOfDay = true WHERE s.intraDay != null")
    void updateHasEndOfDay();

    @Override
    @Modifying
    @Query("DELETE FROM Stock s WHERE s.id = ?1")
    int deleteStockById(Long id);

    @Override
    @Modifying
    @Query("DELETE FROM Stock s WHERE s.tag = ?1")
    int deleteByStockTag(String stockTag);
}