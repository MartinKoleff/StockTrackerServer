package com.koleff.stockserver.stocks.configuration;

import com.koleff.stockserver.StockServerApplication;
import com.koleff.stockserver.stocks.InfoApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AppConfig {

    private final static Logger logger = LogManager.getLogger(AppConfig.class);

    @Value("${app.useFakeRepository:false}")
    private Boolean useFakeRepository;

    @Value("${info.company.name}")
    private String companyName;

    @Autowired
    private Environment environment;

    @Bean
    CommandLineRunner commandLineRunner(InfoApp infoApp) {
        return args -> {
            logger.info(companyName);
            logger.info(environment.getProperty("info.app.version"));
            logger.info(String.format("%s | %s | %s\n",
                    infoApp.getName(),
                    infoApp.getDescription(),
                    infoApp.getVersion()
            ));
        };
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("PublicApi-");
        executor.initialize();
        return executor;
    }
}

