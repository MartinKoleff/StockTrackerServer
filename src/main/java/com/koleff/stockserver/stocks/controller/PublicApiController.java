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
    @GetMapping("{databaseTable}/get/{stockTag}")
    public DataWrapper<T> getData(@PathVariable("databaseTable") String databaseTable,
                                  @PathVariable("stockTag") String stockTag) {
        return publicApiClient.getData(databaseTable, stockTag);
    }

    /**
     * Save to DB
     */
    @PutMapping("{databaseTable}/save/{stockTag}")
    public void saveData(@PathVariable("databaseTable") String databaseTable,
                         @PathVariable("stockTag") String stockTag) {
        publicApiService.saveData(databaseTable, stockTag);
    }

    /**
     * Save all to DB
     */
    @PutMapping("{databaseTable}/save/all")
    public void saveBulkData(@PathVariable("databaseTable") String databaseTable) {
        publicApiService.saveBulkData(databaseTable);
    }

    /**
     * Load from JSON
     */
    @GetMapping("{databaseTable}/load/{stockTag}")
    public void loadData(@PathVariable("databaseTable") String databaseTable,
                         @PathVariable("stockTag") String stockTag) {
        publicApiService.loadData(databaseTable, stockTag);
    }

    /**
     * Load all from JSON
     */
    @GetMapping("{databaseTable}/load/all")
    public void loadBulkData(@PathVariable("databaseTable") String databaseTable) {
        publicApiService.loadBulkData(databaseTable);
    }
}
