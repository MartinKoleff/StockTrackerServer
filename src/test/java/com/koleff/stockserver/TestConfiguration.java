package com.koleff.stockserver;

import com.koleff.stockserver.stocks.repository.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {
        StockRepository.class,
        IntraDayRepository.class,
        EndOfDayRepository.class,
        StockExchangeRepository.class,
        CurrencyRepository.class,
        TimezoneRepository.class
    }
)
public class TestConfiguration {
}
