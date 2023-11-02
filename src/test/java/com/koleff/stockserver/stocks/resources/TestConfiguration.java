package com.koleff.stockserver.stocks.resources;

import com.koleff.stockserver.stocks.repository.*;
import com.koleff.stockserver.stocks.repository.impl.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.koleff.stockserver.stocks.repository"
)
public class TestConfiguration {
}
