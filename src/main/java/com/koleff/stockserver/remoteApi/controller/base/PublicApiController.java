package com.koleff.stockserver.remoteApi.controller.base;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;

public abstract class PublicApiController<T>  {
    private final PublicApiServiceImpl<T> publicApiServiceImpl;

    public PublicApiController(PublicApiServiceImpl<T> publicApiServiceImpl) {
        this.publicApiServiceImpl = publicApiServiceImpl;
    }

    /**
     * Get from remote API
     */
    public DataWrapper<T> getData(String stockTag) {
        return publicApiServiceImpl.getData(stockTag);
    }

    /**
     * Get from remote API and export to JSON
     */
    public void exportDataToJson(String stockTag) {
        DataWrapper<T> response = publicApiServiceImpl.getData(stockTag); //TODO: add data as dependency
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
