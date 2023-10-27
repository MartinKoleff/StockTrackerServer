package com.koleff.stockserver.stocks.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonUtilConfig {

    @Bean("gson")
    public Gson gson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
