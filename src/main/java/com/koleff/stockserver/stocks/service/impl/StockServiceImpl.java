package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.dto.mapper.StockDtoMapper;
import com.koleff.stockserver.stocks.exceptions.DBEmptyException;
import com.koleff.stockserver.stocks.exceptions.StockNotFoundException;
import com.koleff.stockserver.stocks.exceptions.StocksNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.StockRepositoryImpl;
import com.koleff.stockserver.stocks.service.StockService;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Value("${koleff.versionAnnotation}") //Configuring version annotation for Json loading / exporting
    private String versionAnnotation;
    private final StockRepositoryImpl stockRepositoryImpl;
    private final StockDtoMapper stockDtoMapper;
    private final JsonUtil<DataWrapper<Stock>> jsonUtil;

    @Autowired
    public StockServiceImpl(StockRepositoryImpl stockRepositoryImpl,
                            StockDtoMapper stockDtoMapper,
                            JsonUtil<DataWrapper<Stock>> jsonUtil) {
        this.stockRepositoryImpl = stockRepositoryImpl;
        this.stockDtoMapper = stockDtoMapper;
        this.jsonUtil = jsonUtil;
    }

    /**
     * Get stock from DB via id
     */
    @Override
    public StockDto getStock(Long id) {
        return stockRepositoryImpl.findById(id)
                .stream()
                .map(stockDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new StockNotFoundException(
                                String.format("Stock with id %d not found.",
                                        id
                                )
                        )
                );
    }

    /**
     * Get stock from DB via stockTag and map to DTO
     */
    @Override
    public StockDto getStockDto(String stockTag) {
        return stockRepositoryImpl.findStockByStockTag(stockTag)
                .stream()
                .map(stockDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new StockNotFoundException(
                                String.format("Stock with tag %s not found.",
                                        stockTag
                                )
                        )
                );
    }

    /**
     * Get stock from DB via stockTag
     */
    @Override
    public Stock getStock(String stockTag) {
        return stockRepositoryImpl.findStockByStockTag(stockTag)
                .stream()
                .findFirst()
                .orElseThrow(
                        () -> new StockNotFoundException(
                                String.format("Stock with tag %s not found.",
                                        stockTag
                                )
                        )
                );
    }

    /**
     * Get stock id from DB via stockTag
     */
    @Override
    public Long getStockId(String stockTag) {
        return stockRepositoryImpl.findStockByStockTag(stockTag)
                .stream()
                .findFirst()
                .orElseThrow(
                        () -> new StockNotFoundException(
                                String.format("Stock with tag %s not found.",
                                        stockTag
                                )
                        )
                ).getId();
    }

    /**
     * Get all stocks from DB
     */
    @Override
    public List<StockDto> getStocks() {
        return stockRepositoryImpl.findAll()
                .stream()
                .map(stockDtoMapper)
                .toList();
    }

    /**
     * Get stock tags column from DB
     */
    @Override
    public List<String> getStockTags() {
        return stockRepositoryImpl.getStockTags()
                .orElseThrow(
                        () -> new StocksNotFoundException("Stocks not found. Please load them.")
                )
                .stream()
                .toList();
    }

    @Override
    public List<Long> getStockIds() {
        return stockRepositoryImpl.getStockIds()
                .orElseThrow(
                        () -> new DBEmptyException("Stock DB is empty.")
                )
                .stream()
                .toList();
    }

    /**
     * Load stock tags column from JSON
     */
    @Override
    public List<String> loadStockTags(){
       return loadAllStocks()
                .stream()
                .map(Stock::getTag)
                .toList();
    }

    /**
     * Save one entry to DB
     */
    @Override
    public void saveStock(Stock stock) {
        stockRepositoryImpl.save(stock);
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveStocks(List<Stock> data) {
        stockRepositoryImpl.saveAll(data);
    }

    /**
     * Delete entry from DB via id
     */
    @Override
    public void deleteById(Long id) {
//        stockRepositoryImpl.deleteStockById(id);
        stockRepositoryImpl.deleteById(id);
    }

    /**
     * Delete entry from DB via stockTag
     */
    @Override
    public void deleteByStockTag(String stockTag) {
        stockRepositoryImpl.deleteByStockTag(stockTag);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        stockRepositoryImpl.deleteAll();
    }

    /**
     * Load one entry from JSON
     */
    @Override
    public Stock loadStock(String stockTag) {
        String filePath = String.format("tickers%s.json", versionAnnotation);

        String json = jsonUtil.loadJson(filePath);

        DataWrapper<Stock> data = jsonUtil.convertJson(json);

        Stock stockData = data.getData()
                .stream()
                .filter(stock -> stock.getTag().equals(stockTag))
                .findFirst()
                .orElseThrow(
                        () -> new StockNotFoundException("Stock not found in the JSON with all stocks.")
                );

        return stockData;
    }

    /**
     * Load data from JSON
     */
    @Override
    public List<Stock> loadAllStocks() {
        String json = jsonUtil.loadJson("tickersV2.json");

        DataWrapper<Stock> data = jsonUtil.convertJson(json);

        return data.getData();
    }
}
