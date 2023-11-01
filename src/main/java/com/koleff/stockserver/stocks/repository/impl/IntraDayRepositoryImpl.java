package com.koleff.stockserver.stocks.repository.impl;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.repository.IntraDayRepository;
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
public interface IntraDayRepositoryImpl extends IntraDayRepository {

    @Override
    @Query(
            value = "SELECT id FROM IntraDay id " +
                    "JOIN Stock s ON (s.id = id.stockId) " +
                    "WHERE s.tag = ?1"
    )
    Optional<List<IntraDay>> findIntraDayByStockTag(String stockTag, String dateFrom, String dateTo);


    @Override
    @Query(
            value = "SELECT id FROM IntraDay id " +
                    "JOIN Stock s ON (s.id = id.stockId) " +
                    "WHERE s.tag = ?1 AND " +
                    "id.date BETWEEN ?2 AND ?3"
    )
    Optional<List<IntraDay>> findIntraDayByStockTag(String stockTag);

    @Override
    @Query(
            value = "SELECT id FROM IntraDay id " +
                    "JOIN Stock s ON (s.id = id.stockId) " +
                    "WHERE s.tag = ?1 AND " +
                    "id.date = ?2"
    )
    Optional<List<IntraDay>> findIntraDayByStockTag(String stockTag, String date);

    @Override
    @Query(
            value = "SELECT id FROM IntraDay id " +
                    "WHERE id.id = ?1"
    )
    Optional<List<IntraDay>> findAllById(Long stockId);

    @Override
    @Modifying
    @Query(
            value = "DELETE FROM intra_day id " +
                    "USING stock s " +
                    "WHERE s.tag = $1",
            nativeQuery = true
    )
    int deleteByStockTag(String stockTag);
}

