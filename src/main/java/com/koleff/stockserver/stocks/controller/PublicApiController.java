package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.client.PublicApiClientV2;
import com.koleff.stockserver.stocks.domain.SupportTable;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.service.PublicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "publicApi/v1/")
public class PublicApiController<T extends SupportTable> {

    @Value("${apiKey}")
    private String apiKey;
    private final PublicApiService<T> publicApiService;
    private final PublicApiClientV2<T> publicApiClientV2;

    @Autowired
    public PublicApiController(PublicApiService<T> publicApiService,
                               PublicApiClientV2<T> publicApiClientV2) {
        this.publicApiService = publicApiService;
        this.publicApiClientV2 = publicApiClientV2;
    }

    /**
     * Get from remote API
     */
    @GetMapping("{databaseTable}/get/{stockTag}")
    public DataWrapper<T> getData(@PathVariable("databaseTable") String databaseTable,
                                  @PathVariable("stockTag") String stockTag) {
        return publicApiClientV2.getData(apiKey, stockTag, databaseTable);
    }
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
