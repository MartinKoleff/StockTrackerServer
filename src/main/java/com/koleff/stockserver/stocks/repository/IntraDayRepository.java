package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.IntraDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface IntraDayRepository extends JpaRepository<IntraDay, Long> {

    @Query(
            value = "SELECT * FROM intra_day id " +
                    "JOIN stock s ON (s.id = id.stock_id) " +
                    "WHERE s.tag = 1?",
            nativeQuery = true)
    Optional<List<IntraDay>> findIntraDayByStockTag(String stockTag);

    @Query(
            value = "SELECT * FROM intra_day id " +
                    "WHERE id.id = 1?",
            nativeQuery = true)
    Optional<List<IntraDay>> findAllById(Long stockId);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM intra_day id " +
                    "JOIN stock s ON (s.id = id.stock_id) " +
                    "WHERE s.tag = 1?",
            nativeQuery = true)
    int deleteByStockTag(String stockTag);
}

