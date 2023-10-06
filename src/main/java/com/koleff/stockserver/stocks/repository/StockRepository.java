package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s WHERE s.tag = ?1")
    Optional<Stock> findStockByStockTag(String stockTag);

    @Query(
            value = "SELECT * FROM stock WHERE has_intra_day = :hasIntraDay",
            nativeQuery = true)
    List<Stock> selectStockWhereHasIntraDayEqualTrue(
            @Param("hasIntraDay") Boolean hasIntraDay
    );

    @Query(
            value = "SELECT * FROM stock WHERE has_end_of_day = :hasEndOfDay",
            nativeQuery = true)
    List<Stock> selectStockWhereHasEndOfDayEqualTrue(
            @Param("hasEndOfDay") Boolean hasEndOfDay
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM Stock s WHERE s.id = ?1")
    int deleteStockById(Long id);
}