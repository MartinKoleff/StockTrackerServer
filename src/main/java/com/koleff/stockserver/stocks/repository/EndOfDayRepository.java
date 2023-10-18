package com.koleff.stockserver.stocks.repository;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.IntraDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true,
               rollbackFor = Exception.class,
               propagation = Propagation.REQUIRED
)
public interface EndOfDayRepository extends JpaRepository<EndOfDay, Long> {
}

