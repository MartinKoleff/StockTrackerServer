package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.IntraDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndOfDayRepository extends JpaRepository<EndOfDay, Long> {
}

