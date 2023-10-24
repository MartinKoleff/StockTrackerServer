package com.koleff.stockserver.remoteApi.service.impl.base;

import com.koleff.stockserver.remoteApi.client.v2.base.PublicApiClientV2;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.stocks.exceptions.JsonNotFoundException;
import com.koleff.stockserver.remoteApi.service.PublicApiService;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PublicApiServiceImpl<T>
        implements PublicApiService<T> {

    @Value("${apiKey}")
    private String apiKey;

    @Value("${koleff.versionAnnotation}") //Configuring version annotation for Json loading / exporting
    private String versionAnnotation;
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
        return publicApiClientV2.getData(new DatabaseTableDto(getRequestName()), apiKey, stockTag);
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

        System.out.printf("Data successfully added to DB!\nData: %s\n", data);
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
            throw new JsonNotFoundException("JSON file doesn't exist. Please call export request first.");
        }

        //Parse JSON to entity
        DataWrapper<T> data = jsonUtil.convertJson(json);
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
        stockTags.forEach(this::saveData);
    }
}

