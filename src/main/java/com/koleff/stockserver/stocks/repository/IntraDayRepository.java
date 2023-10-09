package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.IntraDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntraDayRepository extends JpaRepository<IntraDay, Long> {

}

