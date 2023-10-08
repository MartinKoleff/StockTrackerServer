package com.koleff.stockserver.stocks.configuration;

import com.koleff.stockserver.stocks.InfoApp;
import com.koleff.stockserver.stocks.dto.wrapper.StocksWrapper;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class StockConfig {

    @Value("${app.useFakeStockRepository:false}")
    private Boolean useFakeStockRepository;

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

            JsonUtil<StocksWrapper> jsonParser = new JsonUtil<StocksWrapper>(StocksWrapper.class);
            String json = jsonParser.getJson("tickersResponse.json");
            StocksWrapper data = jsonParser.convertJson(json);

            System.out.println(data.getStockList());
        };
    }
}

