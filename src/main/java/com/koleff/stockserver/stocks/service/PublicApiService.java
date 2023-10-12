package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.SupportTable;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.exceptions.DataNotSavedException;
import com.koleff.stockserver.stocks.exceptions.JsonNotFoundException;
import com.koleff.stockserver.stocks.repository.IntraDayRepository;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class PublicApiService<T extends SupportTable> {
    private final JsonUtil<DataWrapper<T>> jsonUtil;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final IntraDayRepository intraDayRepository;
    @Autowired
    public PublicApiService(
            StockRepository stockRepository,
            StockService stockService,
            IntraDayRepository intraDayRepository,
            JsonUtil<DataWrapper<T>> jsonUtil) {
        this.stockRepository = stockRepository;
        this.stockService = stockService;
        this.intraDayRepository = intraDayRepository;
        this.jsonUtil = jsonUtil;

    }

    public void saveData(String databaseTable, String stockTag) {
        //Load data
        List<T> data = loadData(databaseTable, stockTag);

        //Configure stock_id
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
        intraDayRepository.saveAll(data);

        System.out.printf("Data successfully added to DB!\nData: %s\n", data);
    }

    public List<T> loadData(String databaseTable, String stockTag) {
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


    public void exportDataToJson(DataWrapper<T> response, String databaseTable, String stockTag) {
        //Export to JSON
        jsonUtil.setType(JsonUtil.extractType(databaseTable));
        jsonUtil.exportToJson(response, databaseTable, stockTag);
    }

    public void loadBulkData(String databaseTable) {
        //Load stock tags
        List<String> stockTags = loadStockTags();

        //Load all JSON files
        stockTags.forEach(stockTag -> loadData(databaseTable, stockTag));
    }

    public void saveBulkData(String databaseTable) {
        //Load stock tags
        List<String> stockTags = loadStockTags();

        //Run multiple threads...
        stockTags.forEach(stockTag -> saveData(databaseTable, stockTag));
    }

    private List<String> loadStockTags() {
        List<String> stockTags = stockRepository.getStockTags()
                .orElseThrow()
                .stream()
                .toList();

        if (stockTags.isEmpty()) {
            stockService.saveStocks();
            return loadStockTags();
        }

        return stockTags;
    }
}

