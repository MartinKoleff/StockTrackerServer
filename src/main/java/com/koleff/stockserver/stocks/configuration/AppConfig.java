package com.koleff.stockserver.stocks.configuration;

import com.koleff.stockserver.stocks.InfoApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {

    @Value("${app.useFakeRepository:false}")
    private Boolean useFakeRepository;

    @Value("${info.company.name}")
    private String companyName;

    @Autowired
    private Environment environment;

    @Bean
    CommandLineRunner commandLineRunner(InfoApp infoApp) {
        return args -> {
            System.out.println(companyName);
            System.out.println(environment.getProperty("info.app.version"));
            System.out.printf("%s | %s | %s\n",
                    infoApp.getName(),
                    infoApp.getDescription(),
                    infoApp.getVersion()
            );
        };
    }
}
