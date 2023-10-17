package com.koleff.stockserver.remoteApi.controller.base;

import com.koleff.stockserver.remoteApi.client.v2.base.PublicApiClientV2;
import com.koleff.stockserver.stocks.domain.SupportTable;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
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
    public void exportDataToJson(String stockTag) {
        DataWrapper<T> response = publicApiClientV2.getData(apiKey, stockTag);
        publicApiServiceImpl.exportDataToJson(response, stockTag);
    }

    /**
     * Save to DB
     */
    public void saveData(String stockTag) {
        publicApiServiceImpl.saveData(stockTag);
    }

    /**
     * Save all to DB
     */
    public void saveBulkData() {
        publicApiServiceImpl.saveBulkData();
    }

    /**
     * Load from JSON
     */
    public void loadData(String stockTag) {
        publicApiServiceImpl.loadData(stockTag);
    }

    /**
     * Load all from JSON
     */
    public void loadBulkData() {
        publicApiServiceImpl.loadBulkData();
    }
}
