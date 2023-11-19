package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.dto.mapper.StockDtoMapper;
import com.koleff.stockserver.stocks.exceptions.StockNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.StockRepositoryImpl;
import com.koleff.stockserver.stocks.service.StockService;
import com.koleff.stockserver.stocks.utils.jsonUtil.StockJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Value("${koleff.versionAnnotation}") //Configuring version annotation for Json loading / exporting
    private String versionAnnotation;
    private final StockRepositoryImpl stockRepositoryImpl;
    private final JdbcTemplate jdbcTemplate;
    private final StockDtoMapper stockDtoMapper;
    private final StockJsonUtil stockJsonUtil;

    @Autowired
    public StockServiceImpl(StockRepositoryImpl stockRepositoryImpl,
                            JdbcTemplate jdbcTemplate,
                            StockDtoMapper stockDtoMapper,
                            StockJsonUtil stockJsonUtil) {
        this.stockRepositoryImpl = stockRepositoryImpl;
        this.jdbcTemplate = jdbcTemplate;
        this.stockDtoMapper = stockDtoMapper;
        this.stockJsonUtil = stockJsonUtil;
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
        return stockRepositoryImpl.findStockByTag(stockTag)
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
        return stockRepositoryImpl.findStockByTag(stockTag)
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
        return stockRepositoryImpl.findStockByTag(stockTag)
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
    public List<String> getTagsColumn() {
        return stockRepositoryImpl.findAll()
                .stream()
                .map(Stock::getTag)
                .toList();
    }

    /**
     * Get id column from DB
     */
    @Override
    public List<Long> getStockIdsColumn() {
        return stockRepositoryImpl.findAll()
                .stream()
                .map(Stock::getId)
                .toList();
    }

    /**
     * Load stock tags column from JSON
     */
    @Override
    public List<String> loadStockTags() {
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
     * Update hasIntraDay flag in DB
     */
    @Override
    public void updateHasIntraDay(String stockTag) {
        stockRepositoryImpl.updateIntraDayStatus(stockTag);
    }

    /**
     * Update hasEndOfDay flag in DB
     */
    @Override
    public void updateHasEndOfDay(String stockTag) {
        stockRepositoryImpl.updateEndOfDayStatus(stockTag);
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
    public void deleteByTag(String stockTag) {
        stockRepositoryImpl.deleteByTag(stockTag);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        stockRepositoryImpl.deleteAll();
    }

    @Override
    public void truncateTable() {
        stockRepositoryImpl.truncate();

        String sqlStatement = "ALTER SEQUENCE stock_sequence RESTART WITH 1";
        jdbcTemplate.execute(sqlStatement);
    }

    /**
     * Load one entry from JSON
     */
    @Override
    public Stock loadStock(String stockTag) {
        String filePath = String.format("tickers%s.json", versionAnnotation);

        String json = stockJsonUtil.loadJson(filePath);

        DataWrapper<Stock> data = stockJsonUtil.convertJson(json);

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
        String json = stockJsonUtil.loadJson("tickersV2.json");

        DataWrapper<Stock> data = stockJsonUtil.convertJson(json);

        return data.getData();
    }

    @Override
    public void loadAndSaveAllStocks() {
        List<Stock> data = loadAllStocks();

        saveStocks(data);
    }
}
