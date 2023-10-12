package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.client.PublicApiClientV2;
import com.koleff.stockserver.stocks.domain.SupportTable;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.stocks.service.PublicApiService;
import jakarta.validation.Valid;
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
    public DataWrapper<T> getData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto,
                                  @PathVariable("stockTag") String stockTag) {
        return publicApiClientV2.getData(apiKey, stockTag, databaseTableDto);
    }

    /**
     * Get from remote API and export to JSON
     */
    @GetMapping("{databaseTable}/export/{stockTag}")
    public void exportDataToJson(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto,
                                 @PathVariable("stockTag") String stockTag) {
        DataWrapper<T> response = publicApiClientV2.getData(apiKey, stockTag, databaseTableDto);
        publicApiService.exportDataToJson(response, databaseTableDto, stockTag);
    }

    /**
     * Save to DB
     */
    @PutMapping("{databaseTable}/save/{stockTag}")
    public void saveData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto,
                         @PathVariable("stockTag") String stockTag) {
        publicApiService.saveData(databaseTableDto, stockTag);
    }

    /**
     * Save all to DB
     */
    @PutMapping("{databaseTable}/save/all")
    public void saveBulkData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto) {
        publicApiService.saveBulkData(databaseTableDto);
    }

    /**
     * Load from JSON
     */
    @GetMapping("{databaseTable}/load/{stockTag}")
    public void loadData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto,
                         @PathVariable("stockTag") String stockTag) {
        publicApiService.loadData(databaseTableDto, stockTag);
    }

    /**
     * Load all from JSON
     */
    @GetMapping("{databaseTable}/load/all")
    public void loadBulkData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto) {
        publicApiService.loadBulkData(databaseTableDto);
    }
}
