package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;

import java.util.List;

public interface PublicApiService<T> {
    void exportDataToJson(DataWrapper<T> response, DatabaseTableDto databaseTableDto, String stockTag);
    List<T> loadData(DatabaseTableDto databaseTableDto, String stockTag);
    void loadBulkData(DatabaseTableDto databaseTableDto);
    void saveData(DatabaseTableDto databaseTableDto, String stockTag);
    void saveBulkData(DatabaseTableDto databaseTableDto);
}
