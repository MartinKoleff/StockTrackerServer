package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.StockDto;

import java.util.List;

public interface StockService {

    StockDto getStock(Long id);
    StockDto getStock(String stockTag);
    List<StockDto> getStocks();
    List<String> getStockTags();
    Long getStockId(String stockTag);
    void saveStock(Stock stock);
    void saveStocks(List<Stock> data);
    void deleteById(Long id);
    void deleteByStockTag(String stockTag);
    void deleteAll();
    Stock loadStock(String stockTag);
    List<Stock> loadAllStocks();
}
