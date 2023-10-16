package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.client.PublicApiClientV2;
import com.koleff.stockserver.stocks.domain.SupportTable;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.stocks.service.impl.PublicApiServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "publicApi/v1/")
public class PublicApiController<T extends SupportTable> {

    @Value("${apiKey}")
    private String apiKey;
    private final PublicApiServiceImpl<T> publicApiServiceImpl;
    private final PublicApiClientV2 publicApiClientV2;

    @Autowired
    public PublicApiController(PublicApiServiceImpl<T> publicApiServiceImpl,
                               PublicApiClientV2 publicApiClientV2) {
        this.publicApiServiceImpl = publicApiServiceImpl;
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
        publicApiServiceImpl.exportDataToJson(response, databaseTableDto, stockTag);
    }

    /**
     * Save to DB
     */
    @PutMapping("{databaseTable}/save/{stockTag}") //TODO: add data as dependency
    public void saveData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto,
                         @PathVariable("stockTag") String stockTag) {
        publicApiServiceImpl.saveData(databaseTableDto, stockTag);
    }

    /**
     * Save all to DB
     */
    @PutMapping("{databaseTable}/save/all") //TODO: add data as dependency
    public void saveBulkData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto) {
        publicApiServiceImpl.saveBulkData(databaseTableDto);
    }

    /**
     * Load from JSON
     */
    @GetMapping("{databaseTable}/load/{stockTag}")
    public void loadData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto,
                         @PathVariable("stockTag") String stockTag) {
        publicApiServiceImpl.loadData(databaseTableDto, stockTag);
    }

    /**
     * Load all from JSON
     */
    @GetMapping("{databaseTable}/load/all")
    public void loadBulkData(@Valid @PathVariable("databaseTable") DatabaseTableDto databaseTableDto) {
        publicApiServiceImpl.loadBulkData(databaseTableDto);
    }
}
