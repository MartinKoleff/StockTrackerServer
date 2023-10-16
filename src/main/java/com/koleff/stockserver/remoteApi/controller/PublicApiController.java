package com.koleff.stockserver.remoteApi.controller;

import com.koleff.stockserver.remoteApi.client.v2.PublicApiClientV2;
import com.koleff.stockserver.stocks.domain.SupportTable;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.remoteApi.service.impl.PublicApiServiceImpl;
import org.springframework.beans.factory.annotation.Value;

public abstract class PublicApiController<T extends SupportTable>  {

    @Value("${apiKey}")
    private String apiKey;
    private final PublicApiServiceImpl<T> publicApiServiceImpl; //TODO: migrate like Controller/Client
    private final PublicApiClientV2<T> publicApiClientV2;

    public PublicApiController(PublicApiServiceImpl<T> publicApiServiceImpl,
                               PublicApiClientV2<T> publicApiClientV2) {
        this.publicApiServiceImpl = publicApiServiceImpl;
        this.publicApiClientV2 = publicApiClientV2;
    }

    /**
     * Get from remote API
     */
    public DataWrapper<T> getData(String stockTag) {
        return publicApiClientV2.getData(apiKey, stockTag);
    }

    /**
     * Get from remote API and export to JSON
     */
    public void exportDataToJson(DatabaseTableDto databaseTableDto,
                                 String stockTag) {
        DataWrapper<T> response = publicApiClientV2.getData(apiKey, stockTag);
        publicApiServiceImpl.exportDataToJson(response, databaseTableDto, stockTag);
    }

    /**
     * Save to DB
     */
    public void saveData(DatabaseTableDto databaseTableDto,
                         String stockTag) {
        publicApiServiceImpl.saveData(databaseTableDto, stockTag);
    }

    /**
     * Save all to DB
     */
    public void saveBulkData(DatabaseTableDto databaseTableDto) {
        publicApiServiceImpl.saveBulkData(databaseTableDto);
    }

    /**
     * Load from JSON
     */
    public void loadData(DatabaseTableDto databaseTableDto,
                         String stockTag) {
        publicApiServiceImpl.loadData(databaseTableDto, stockTag);
    }

    /**
     * Load all from JSON
     */
    public void loadBulkData(DatabaseTableDto databaseTableDto) {
        publicApiServiceImpl.loadBulkData(databaseTableDto);
    }
}
