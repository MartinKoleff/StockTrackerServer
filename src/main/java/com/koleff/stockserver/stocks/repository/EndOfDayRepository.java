package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EndOfDayRepository extends JpaRepository<EndOfDay, Long> {

    Optional<List<EndOfDay>> findEndOfDayByStockTag(String stockTag);

    Optional<List<EndOfDay>> findAllById(Long stockId);

    int deleteByStockTag(String stockTag);
}
