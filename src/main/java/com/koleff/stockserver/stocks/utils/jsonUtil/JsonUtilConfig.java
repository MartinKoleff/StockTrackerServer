package com.koleff.stockserver.stocks.utils.jsonUtil;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonUtilConfig {

    @Bean("gson")
    public Gson gson(){
        return new Gson();
    }
}
