package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.client.PublicApiClient;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.service.PublicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "publicApi/v1/")
public class PublicApiController {

    private final PublicApiService publicApiService;
    private final PublicApiClient publicApiClient;

    @Autowired
    public PublicApiController(PublicApiService publicApiService,
                               PublicApiClient publicApiClient) {
        this.publicApiService = publicApiService;
        this.publicApiClient = publicApiClient;
    }

    /**
     * Get from remote API
     */
    @GetMapping("intraday/get/{stockTag}")
    public DataWrapper<IntraDay> getIntraDay(@PathVariable("stockTag") String stockTag) {
        return publicApiClient.getIntraDay(stockTag);
    }

    /**
     * Save to DB
     */
    @PutMapping("intraday/save/{stockTag}")
    public void saveIntraDay(@PathVariable("stockTag") String stockTag) {
        publicApiService.saveIntraDay(stockTag);
    }

    /**
     * Save all to DB
     */
    @PutMapping("intraday/save/all")
    public void saveIntraDays() {
        publicApiService.saveIntraDays();
    }

    /**
     * Load from JSON
     */
    @GetMapping("intraday/load/{stockTag}")
    public void loadIntraDay(@PathVariable("stockTag") String stockTag) {
        publicApiService.loadIntraDay(stockTag);
    }

    /**
     * Load all from JSON
     */
    @GetMapping("intraday/load/all")
    public void loadIntraDays() {
        publicApiService.loadIntraDays();
    }
}
