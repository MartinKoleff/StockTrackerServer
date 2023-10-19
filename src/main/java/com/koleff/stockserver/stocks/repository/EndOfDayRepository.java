package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
public interface EndOfDayRepository extends JpaRepository<EndOfDay, Long> {
    @Query(
            value = "SELECT * FROM end_of_day eod " +
                    "JOIN stock s ON (s.id = eod.stock_id) " +
                    "WHERE s.tag = 1?",
            nativeQuery = true)
    Optional<List<EndOfDay>> findEndOfDayByStockTag(String stockTag);

    @Query(
            value = "SELECT * FROM end_of_day eod " +
                    "WHERE eod.id = 1?",
            nativeQuery = true)
    Optional<List<EndOfDay>> findAllById(Long stockId);

    @Modifying
    @Query(
            value = "DELETE FROM end_of_day eod " +
                    "JOIN stock s ON (s.id = eod.stock_id) " +
                    "WHERE s.tag = 1?",
            nativeQuery = true)
    int deleteByStockTag(String stockTag);
}

