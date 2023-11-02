package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.IntraDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IntraDayRepository extends JpaRepository<IntraDay, Long> {

    Optional<List<IntraDay>> findIntraDayByStockTag(String stockTag);
    Optional<List<IntraDay>> findIntraDayByStockTagAndDateBetween(String stockTag, String dateFrom, String dateTo);
    Optional<List<IntraDay>> findIntraDayByStockTagAndDate(String stockTag, String date);

    Optional<List<IntraDay>> findAllById(Long stockId);

    int deleteByStockTag(String stockTag);
    void truncate();
}
