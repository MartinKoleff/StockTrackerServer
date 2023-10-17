package com.koleff.stockserver.remoteApi.service;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;

import java.util.List;

public interface PublicApiService<T> {
    void exportDataToJson(DataWrapper<T> response, String stockTag);
    List<T> loadData(String stockTag);
    void loadBulkData();
    void saveData(String stockTag);
    void saveBulkData();
}
