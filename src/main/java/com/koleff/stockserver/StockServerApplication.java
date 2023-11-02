package com.koleff.stockserver;

import com.koleff.stockserver.remoteApi.client.v2.EndOfDayPublicApiClientV2;
import com.koleff.stockserver.remoteApi.client.v2.IntraDayPublicApiClientV2;
import com.koleff.stockserver.remoteApi.client.v2.StockExchangePublicApiClientV2;
import com.koleff.stockserver.remoteApi.client.v2.StockPublicApiClientV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(
        exclude = {
                ElasticsearchRestClientAutoConfiguration.class,
//                DataSourceAutoConfiguration.class,
//                HibernateJpaAutoConfiguration.class
        }
)
@ConfigurationPropertiesScan
//@ComponentScan(basePackages = "com.koleff.stockserver.stocks.repository")
@EnableJpaRepositories(
        basePackages = "com.koleff.stockserver.stocks.repository",
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND
)
@EnableFeignClients(
        clients = {
                EndOfDayPublicApiClientV2.class,
                IntraDayPublicApiClientV2.class,
                StockPublicApiClientV2.class,
                StockExchangePublicApiClientV2.class
        }
)
@EnableConfigurationProperties
@EnableAsync
@EnableWebMvc
public class StockServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockServerApplication.class, args);
    }
}
