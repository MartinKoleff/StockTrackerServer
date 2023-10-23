package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findStockByStockTag(String stockTag);

    Optional<List<String>> getStockTags();

    List<Stock> selectStocksWhereHasIntraDayEqualTrue(
            @Param("hasIntraDay") Boolean hasIntraDay
    );

    List<Stock> selectStocksWhereHasEndOfDayEqualTrue(
            @Param("hasEndOfDay") Boolean hasEndOfDay
    );

    int deleteStockById(Long id);

    int deleteByStockTag(String stockTag);
}
