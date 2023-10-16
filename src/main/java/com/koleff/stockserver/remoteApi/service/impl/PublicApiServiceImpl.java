package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.stocks.domain.SupportTable;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.stocks.exceptions.JsonNotFoundException;
import com.koleff.stockserver.remoteApi.service.PublicApiService;
import com.koleff.stockserver.stocks.service.impl.EndOfDayServiceImpl;
import com.koleff.stockserver.stocks.service.impl.IntraDayServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockExchangeServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class PublicApiServiceImpl<T extends SupportTable>
        implements PublicApiService<T> {
    private final StockServiceImpl stockServiceImpl;
    private final IntraDayServiceImpl intraDayServiceImpl;
    private final EndOfDayServiceImpl endOfDayServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final JsonUtil<DataWrapper<T>> jsonUtil;

    @Autowired
    public PublicApiServiceImpl(
            StockServiceImpl stockServiceImpl,
            IntraDayServiceImpl intraDayServiceImpl,
            EndOfDayServiceImpl endOfDayServiceImpl,
            StockExchangeServiceImpl stockExchangeServiceImpl,
            JsonUtil<DataWrapper<T>> jsonUtil) {
        this.stockServiceImpl = stockServiceImpl;
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.endOfDayServiceImpl = endOfDayServiceImpl;
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.jsonUtil = jsonUtil;
    }

    /**
     * Configures join IDs and saves data to repository DB
     *
     * @param databaseTableDto type of request
     * @param stockTag         stock tag
     */
    @Override
    public void saveData(DatabaseTableDto databaseTableDto, String stockTag) {
        //Load data
        String databaseTable = databaseTableDto.databaseTable();
        List<T> data = loadData(databaseTableDto, stockTag);

        //Configure stock_id/ stock_exchange_id? //TODO: refactor SupportTable
        data.forEach(entity -> {
            entity.setStockId(
                    stockServiceImpl.getStockId(stockTag)
            );
        });

        //Save data entities to DB
        saveToRepository(databaseTable, data);

        System.out.printf("Data successfully added to DB!\nData: %s\n", data);
    }

    /**
     * Save data to repository DB
     *
     * @param databaseTable type of request
     * @param data          entry
     */
    private void saveToRepository(String databaseTable, List<T> data) {
        //TODO: Assert T is IntraDay/EndOfDay/StockExchangeService...
//        switch () {
//            case "tickers":
//                stockServiceImpl.saveStocks(data);
//                break;
//            case "intraday":
//                intraDayServiceImpl.saveAllIntraDays(data);
//                break;
//            case "eod":
//                endOfDayServiceImpl.saveAllEndOfDays(data);
//                break;
//            case "exchange":
//                stockExchangeServiceImpl.saveStockExchanges(data);
//                break;
//        }
    }

    /**
     * Loads data from JSON file and parses it to entity.
     * - Used for EndOfDay and IntraDay Entities because they use multiple JSON files.
     * - Not used for StockExchangeService and Stock Entities because they use 1 JSON file.
     *
     * @param databaseTableDto type of request
     * @param stockTag         stock tag
     */
    @Override
    public List<T> loadData(DatabaseTableDto databaseTableDto, String stockTag) {
        String databaseTable = databaseTableDto.databaseTable();

        //Find JSON file
        String filePath = String.format("%s%s.json",
                databaseTable,
                stockTag);

        //Load data from JSON
        jsonUtil.setType(JsonUtil.extractType(databaseTable));
        String json = jsonUtil.loadJson(filePath);

        if (Objects.isNull(json)) {
            throw new JsonNotFoundException("JSON file doesn't exist. Please call export request first.");
        }

        //Parse JSON to entity
        DataWrapper<T> data = jsonUtil.convertJson(json);
        return data.getData();
    }

    /**
     * Exports data to JSON
     *
     * @param response         remote API response
     * @param databaseTableDto type of request
     * @param stockTag         stock tag
     */
    @Override
    public void exportDataToJson(DataWrapper<T> response, DatabaseTableDto databaseTableDto, String stockTag) {
        String databaseTable = databaseTableDto.databaseTable();

        //Export to JSON
        jsonUtil.setType(JsonUtil.extractType(databaseTable));
        jsonUtil.exportToJson(response, databaseTable, stockTag);
    }

    /**
     * Loads all JSON files associated with the @databaseTableDto
     * - Used for EndOfDay and IntraDay JSONs.
     *
     * @param databaseTableDto type of request
     */
    @Override
    public void loadBulkData(DatabaseTableDto databaseTableDto) {
        //Load stock tags
        List<String> stockTags = stockServiceImpl.getStockTags();

        //Load all JSON files
        stockTags.forEach(stockTag -> loadData(databaseTableDto, stockTag));
    }

    /**
     * Saves all entries associated with @databaseTableDto to their repository DB
     *
     * @param databaseTableDto type of request
     */
    @Override
    public void saveBulkData(DatabaseTableDto databaseTableDto) {
        //Load stock tags
        List<String> stockTags = stockServiceImpl.getStockTags();

        //TODO: Run multiple threads...
        stockTags.forEach(stockTag -> saveData(databaseTableDto, stockTag));
    }
}

