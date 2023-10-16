package com.koleff.stockserver;

import com.koleff.stockserver.stocks.repository.EndOfDayRepository;
import com.koleff.stockserver.stocks.repository.IntraDayRepository;
import com.koleff.stockserver.stocks.repository.StockExchangeRepository;
import com.koleff.stockserver.stocks.repository.StockRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {
        StockRepository.class,
        IntraDayRepository.class,
        EndOfDayRepository.class,
        StockExchangeRepository.class
        //TODO: add more in future...
    }
)
public class TestConfiguration {
}
