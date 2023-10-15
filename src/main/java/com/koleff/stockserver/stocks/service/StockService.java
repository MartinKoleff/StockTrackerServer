package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.dto.mapper.StockDtoMapper;
import com.koleff.stockserver.stocks.exceptions.IntraDayNotSavedException;
import com.koleff.stockserver.stocks.exceptions.StockNotFoundException;
import com.koleff.stockserver.stocks.exceptions.StocksNotFoundException;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockDtoMapper stockDtoMapper;
    private final JsonUtil<DataWrapper<Stock>> jsonUtil;

    @Autowired
    public StockService(StockRepository stockRepository,
                        StockDtoMapper stockDtoMapper,
                        JsonUtil<DataWrapper<Stock>> jsonUtil) {
        this.stockRepository = stockRepository;
        this.stockDtoMapper = stockDtoMapper;
        this.jsonUtil = jsonUtil;
    }

    public List<StockDto> getStocks() {
        return stockRepository.findAll()
                .stream()
                .map(stockDtoMapper)
                .toList();
    }

    public StockDto getStock(Long id) {
        return stockRepository.findById(id)
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

    public StockDto getStock(String stockTag) {
        return stockRepository.findStockByStockTag(stockTag)
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

    public List<String> getStockTags() {
        return stockRepository.getStockTags()
                .orElseThrow(
                        () -> new StocksNotFoundException("Stocks not found. Please load them.")
                )
                .stream()
                .toList();
    }

    public void saveStock(Stock stock) {
        stockRepository.save(stock);
    }

    public void saveStocks(List<Stock> data) {
        stockRepository.saveAll(data);
    }

    /**
     * Delete entry from DB via id
     */
    public void delete(Long id) {
//        stockRepository.deleteStockById(id);
        stockRepository.deleteById(id);
    }

    /**
     * Delete all entries in DB
     */
    public void deleteAll() {
        stockRepository.deleteAll();
    }

    //Load data from JSON
    public Stock loadStock(String stockTag) {
        jsonUtil.setType(Stock.class);
        String json = jsonUtil.loadJson("tickers.json");

        DataWrapper<Stock> data = jsonUtil.convertJson(json);

        Stock stockData = data.getData()
                .stream()
                .filter(stock -> stock.getTag().equals(stockTag))
                .findFirst()
                .orElseThrow(
                        () -> new StockNotFoundException("Stock not found in the JSON with all stocks.")
                );
        System.out.println(stockData);

        return stockData;
    }

    //Load data from JSON
    public List<Stock> loadStocks() {
        jsonUtil.setType(Stock.class);
        String json = jsonUtil.loadJson("tickers.json");

        DataWrapper<Stock> data = jsonUtil.convertJson(json);
        System.out.println(data);

        return data.getData();
    }
}
