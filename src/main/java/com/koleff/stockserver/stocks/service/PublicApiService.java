package com.koleff.stockserver.stocks.service;

import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.client.PublicApiClient;
import com.koleff.stockserver.stocks.controller.StockController;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.exceptions.IntraDayNotSavedException;
import com.koleff.stockserver.stocks.repository.IntraDayRepository;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;


@Service
public class PublicApiService {

    private final JsonUtil<DataWrapper> jsonUtil;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final IntraDayRepository intraDayRepository;
    private final PublicApiClient publicApiClient;

    @Autowired
    public PublicApiService(
            StockRepository stockRepository,
            StockService stockService,
            IntraDayRepository intraDayRepository,
            PublicApiClient publicApiClient,
            JsonUtil<DataWrapper> jsonUtil) {
        this.stockRepository = stockRepository;
        this.stockService = stockService;
        this.intraDayRepository = intraDayRepository;
        this.publicApiClient = publicApiClient;
        this.jsonUtil = jsonUtil;
    }

    public void saveIntraDay(String stockTag) {
        //Load data
        List<IntraDay> data = loadIntraDay(stockTag);

        //Configure stock_id
        data.forEach(intraDay -> {
            intraDay.setStockId(
                    stockRepository.findStockByStockTag(stockTag)
                            .orElseThrow(
                                    () -> new IntraDayNotSavedException(
                                            String.format(
                                                    "Intra day for stock %s could not be saved.",
                                                    stockTag
                                            )
                                    )
                            )
                            .getId()
            );
        });

        //Save data entities to DB
        intraDayRepository.saveAll(data);

        System.out.printf("Data successfully added to DB!\nData: %s\n",data);
    }

    /**AUTOMATE FOR ALL ENTITIES*/
    public List<IntraDay> loadIntraDay(String stockTag) {
        //Configure JsonUtil
        Type intraDayType = new TypeToken<DataWrapper<IntraDay>>() {
        }.getType();
        jsonUtil.setType(intraDayType);

        //Find JSON file
        String filePath = String.format("intraday%s.json", stockTag);

        //Load data from json
        String json = jsonUtil.loadJson(filePath);
        if (Objects.isNull(json)) {

            //Call client to create json...
            publicApiClient.saveIntraDayToJSON(stockTag);

            //Re-call...
            return loadIntraDay(stockTag);
        }

        //Parse json to entity
        DataWrapper<IntraDay> data = jsonUtil.convertJson(json);
        return data.getData();
    }

    public void loadIntraDays() {
        //Load stock tags
        List<String> stockTags = loadStockTags();

        //Load all JSON files
        stockTags.forEach(this::loadIntraDay);
    }

    public void saveIntraDays() {
        //Load stock tags
        List<String> stockTags = loadStockTags();

        stockTags.forEach(this::saveIntraDay);
    }

    private List<String> loadStockTags() {
        List<String> stockTags = stockRepository.getStockTags()
                .orElseThrow()
                .stream()
                .toList();

        if(stockTags.isEmpty()){
            stockService.saveStocks();
            return loadStockTags();
        }

        return stockTags;
    }
}

