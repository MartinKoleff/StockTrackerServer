package com.koleff.stockserver;

import com.koleff.stockserver.stocks.client.v2.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaRepositories
@EnableFeignClients(
        clients = {
                EndOfDayPublicApiClientV2.class,
                IntraDayPublicApiClientV2.class,
                StockPublicApiClientV2.class,
                StockExchangePublicApiClientV2.class
        }
)
@EnableConfigurationProperties
public class StockServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockServerApplication.class, args);
    }
}
