package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerRepository extends JpaRepository<Ticker, Long> {

}