package com.koleff.stockserver.stocks.resources;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.koleff.stockserver.stocks.repository"
)
@ComponentScan(basePackages = "com.koleff.stockserver")
public class TestConfiguration {
}
