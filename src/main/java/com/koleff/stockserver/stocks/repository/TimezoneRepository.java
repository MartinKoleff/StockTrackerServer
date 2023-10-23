package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Timezone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimezoneRepository extends JpaRepository<Timezone, Long> {

    Optional<Timezone> findByStockTag(String stockTag);

    Optional<List<String>> getTimezoneStrings();

    Collection<Timezone> findTimezoneByTimezoneString(String timezone);

    void deleteByTimezone(String timezone);
}
