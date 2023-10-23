package com.koleff.stockserver.remoteApi.service;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;

import java.util.List;

public interface PublicApiService<T> {
    void exportDataToJson(DataWrapper<T> response, String stockTag);
    List<T> loadData(String stockTag);
    List<List<T>> loadBulkData();
    void saveData(String stockTag);
    void saveBulkData();
    DataWrapper<T> getData(String stockTag);
}
