package com.koleff.stockserver.remoteApi.service.impl.base;

import com.koleff.stockserver.remoteApi.client.v2.base.PublicApiClientV2;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.stocks.exceptions.JsonNotFoundException;
import com.koleff.stockserver.remoteApi.service.PublicApiService;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PublicApiServiceImpl<T>
        implements PublicApiService<T> {

    @Value("${apiKey}")
    private String apiKey;

    @Value("${koleff.versionAnnotation}") //Configuring version annotation for Json loading / exporting
    private String versionAnnotation;

    private final static Logger logger = LogManager.getLogger(PublicApiServiceImpl.class);

    private final StockServiceImpl stockServiceImpl;
    private final PublicApiClientV2<T> publicApiClientV2;
    private final JsonUtil<DataWrapper<T>> jsonUtil;

    public PublicApiServiceImpl(
            StockServiceImpl stockServiceImpl,
            PublicApiClientV2<T> publicApiClientV2,
            JsonUtil<DataWrapper<T>> jsonUtil) {
        this.stockServiceImpl = stockServiceImpl;
        this.publicApiClientV2 = publicApiClientV2;
        this.jsonUtil = jsonUtil;
    }

    /**
     * Used in exportDataToJSON()
     *
     * @return request name / JSON name prefix
     */
    protected abstract String getRequestName();

    /**
     * Save data for specific entity to repository DB
     */

    protected abstract void saveToRepository(List<T> data);


    /**
     * Configures all secondary IDs used for joins with other tables
     *
     * @param stockTag needed for IntraDay and EndOfDay join
     */
    protected abstract void configureJoin(List<T> data, String stockTag);

    /**
     * Get data from remote API via OpenFeign Client
     */
    @Override
    public DataWrapper<T> getData(String stockTag) {
        DataWrapper<T> data = publicApiClientV2.getData(new DatabaseTableDto(getRequestName()), apiKey, stockTag);

        logger.info(String.format("Data successfully fetched from remote API!\nData: %s\n", data));
        return data;
    }

    /**
     * Configures join IDs and saves data to repository DB
     *
     * @param stockTag stock tag
     */
    @Override
    public void saveData(String stockTag) {
        //Load data
        List<T> data = loadData(stockTag); //TODO: add as dependency and load from tests...

        //Save data entities to DB
        saveToRepository(data);

        logger.info(String.format("Data successfully added to DB!\nData: %s\n", data));
    }


    /**
     * Loads data from JSON file and parses it to entity.
     * - Used for EndOfDay and IntraDay Entities because they use multiple JSON files.
     * - Not used for StockExchangeService and Stock Entities because they use 1 JSON file.
     *
     * @param stockTag stock tag
     */
    @Override
    public List<T> loadData(String stockTag) {
        //Find JSON file
        String filePath = String.format("%s%s%s.json",
                getRequestName(),
                stockTag,
                versionAnnotation);

        //Load data from JSON
        String json = jsonUtil.loadJson(filePath);

        if (Objects.isNull(json)) {
            throw new JsonNotFoundException(String.format("JSON file doesn't exist for stock %s. Please call export request first.", stockTag));
        }

        //Parse JSON to entity
        DataWrapper<T> data = jsonUtil.convertJson(json);

        logger.info(String.format("Data successfully loaded from JSON!\nData: %s\n", data));
        return data.getData();
    }

    /**
     * Exports data to JSON
     *
     * @param response remote API response
     * @param stockTag stock tag
     */
    @Override
    public void exportDataToJson(DataWrapper<T> response, String stockTag) {
        //Configure all joins for entity -> fill all secondary IDs
        configureJoin(response.getData(), stockTag);

        //Export to JSON
        jsonUtil.exportToJson(response, getRequestName(), versionAnnotation, stockTag);
    }

    /**
     * Exports all data to JSON
     * Used for IntraDay and EndOfDay
     */
    @Override
    public void exportAllDataToJson() {
        //Load Stocks...
        List<String> stockTags = stockServiceImpl.loadStockTags(); //Not dependent on DB -> load from JSON

        AtomicInteger counter = new AtomicInteger();
        AtomicInteger delay = new AtomicInteger(1);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(stockTags.size());
        CountDownLatch countDownLatch = new CountDownLatch(stockTags.size());

        //Create JSON V2 with configured data
        stockTags.parallelStream()
                 .forEach(
                        stockTag -> scheduler.schedule(new Runnable() {
                            @Override
                            public void run() {
                                logger.info(String.format("Thread %d has started!\n", counter.getAndIncrement()));

                                DataWrapper<T> data = getData(stockTag);
                                logger.info(String.format("Fetched data from remote API: %s\n", data));

                                exportDataToJson(data, stockTag);

                                logger.info(String.format("CountDownLatch count: %s\n", countDownLatch.getCount()));
                                countDownLatch.countDown();
                            }
                        }, delay.getAndIncrement(), TimeUnit.SECONDS)
                );

        try {
            scheduler.shutdown();
            countDownLatch.await();

            boolean isFinished = scheduler.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all JSON files associated with parametrized type
     * - Used for EndOfDay and IntraDay JSONs.
     */
    @Override
    public List<List<T>> loadBulkData() {
        //Load stock tags
        List<String> stockTags = stockServiceImpl.loadStockTags(); //Not dependent on DB -> load from JSON

        List<List<T>> data = new ArrayList<>();

        //Load all JSON files
        stockTags.forEach(stockTag -> {
                    List<T> entry = loadData(stockTag);

                    data.add(entry);
                }
        );

        return data;
    }

    /**
     * Saves all entries associated with parametrized type to their repository DB
     */
    @Override
    public void saveBulkData() {
        //Load stock tags
        List<String> stockTags = stockServiceImpl.loadStockTags(); //Not dependent on DB -> load from JSON

        //TODO: Run multiple threads...
        stockTags.parallelStream().forEach(this::saveData);
    }
}

