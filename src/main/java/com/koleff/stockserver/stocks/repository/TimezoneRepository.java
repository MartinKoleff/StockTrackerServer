package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.repository.projection.TimezoneProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimezoneRepository extends JpaRepository<Timezone, Long> {

    Optional<Timezone> findByStockExchanges_Stocks_Tag(String tag);

    Collection<Timezone> findTimezoneByTimezone(String timezone);

    void deleteByTimezone(String timezone);
}
