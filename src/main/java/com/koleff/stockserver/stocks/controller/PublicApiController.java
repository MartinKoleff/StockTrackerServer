package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "publicApi/v1/")
public class PublicApiController {

    @Value("${apiKey}")
    private String apiKey;

    @GetMapping("intraday/{stockTag}")
    public DataWrapper getIntraDay(@PathVariable("stockTag") String stockTag){
        String url = String.format("http://api.marketstack.com/v1/intraday?access_key=%s&symbols=%s",
                apiKey,
                stockTag);

        //Call remote API
        RestTemplate restTemplate = new RestTemplate();
        DataWrapper<IntraDay> response = restTemplate.getForObject(url, DataWrapper.class);
        return response;
    }
}
