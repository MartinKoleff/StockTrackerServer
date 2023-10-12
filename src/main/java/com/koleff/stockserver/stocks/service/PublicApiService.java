package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.SupportTable;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.stocks.exceptions.DataNotSavedException;
import com.koleff.stockserver.stocks.exceptions.JsonNotFoundException;
import com.koleff.stockserver.stocks.repository.EndOfDayRepository;
import com.koleff.stockserver.stocks.repository.IntraDayRepository;
import com.koleff.stockserver.stocks.repository.StockExchangeRepository;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class PublicApiService<T extends SupportTable> {
    private final JsonUtil<DataWrapper<T>> jsonUtil;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final IntraDayRepository intraDayRepository;
    private final EndOfDayRepository endOfDayRepository;
    private final StockExchangeRepository stockExchangeRepository;
    @Autowired
    public PublicApiService(
            StockRepository stockRepository,
            StockService stockService,
            IntraDayRepository intraDayRepository,
            EndOfDayRepository endOfDayRepository,
            StockExchangeRepository stockExchangeRepository,
            JsonUtil<DataWrapper<T>> jsonUtil) {
        this.stockRepository = stockRepository;
        this.stockService = stockService;
        this.intraDayRepository = intraDayRepository;
        this.endOfDayRepository = endOfDayRepository;
        this.stockExchangeRepository = stockExchangeRepository;
        this.jsonUtil = jsonUtil;
    }

    public void saveData(DatabaseTableDto databaseTableDto, String stockTag) {
        //Load data
        String databaseTable = databaseTableDto.databaseTable();
        List<T> data = loadData(databaseTableDto, stockTag);

        //Configure stock_id/ stock_exchange_id?
        data.forEach(entity -> {
            entity.setStockId(
                    stockRepository.findStockByStockTag(stockTag)
                            .orElseThrow(
                                    () -> new DataNotSavedException(
                                            String.format(
                                                    "%s for stock %s could not be saved.",
                                                    databaseTable,
                                                    stockTag
                                            )
                                    )
                            )
                            .getId()
            );
        });

        //Save data entities to DB
        saveToRepository(databaseTable, data);

        System.out.printf("Data successfully added to DB!\nData: %s\n", data);
    }

    private void saveToRepository(String databaseTable, List<T> data) {
        switch (databaseTable){
            case"intraday":
                intraDayRepository.saveAll(data);
                break;
            case"eod":
                endOfDayRepository.saveAll(data);
                break;
            case"exchange":
                stockExchangeRepository.saveAll(data);
                break;
        }
    }

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


    public void exportDataToJson(DataWrapper<T> response, DatabaseTableDto databaseTableDto, String stockTag) {
        String databaseTable = databaseTableDto.databaseTable();

        //Export to JSON
        jsonUtil.setType(JsonUtil.extractType(databaseTable));
        jsonUtil.exportToJson(response, databaseTable, stockTag);
    }

    public void loadBulkData(DatabaseTableDto databaseTableDto) {
        //Load stock tags
        List<String> stockTags = stockService.getStockTags();

        //Load all JSON files
        stockTags.forEach(stockTag -> loadData(databaseTableDto, stockTag));
    }

    public void saveBulkData(DatabaseTableDto databaseTableDto) {
        //Load stock tags
        List<String> stockTags = stockService.getStockTags();

        //Run multiple threads...
        stockTags.forEach(stockTag -> saveData(databaseTableDto, stockTag));
    }
}

