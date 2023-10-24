package com.koleff.stockserver.stocks.resources;

import com.koleff.stockserver.stocks.repository.*;
import com.koleff.stockserver.stocks.repository.impl.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {
        StockRepositoryImpl.class,
        IntraDayRepositoryImpl.class,
        EndOfDayRepositoryImpl.class,
        StockExchangeRepositoryImpl.class,
        CurrencyRepositoryImpl.class,
        TimezoneRepositoryImpl.class,
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
