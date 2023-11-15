package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.StockDto;

import java.util.List;

public interface StockService {

    StockDto getStock(Long id);
    StockDto getStockDto(String stockTag);
    Stock getStock(String stockTag);
    List<StockDto> getStocks();
    List<String> getTagsColumn();
    List<Long> getStockIdsColumn();
    List<String> loadStockTags();
    Long getStockId(String stockTag);
    void saveStock(Stock stock);
    void saveStocks(List<Stock> data);
    void deleteById(Long id);
    void deleteByTag(String stockTag);
    void deleteAll();
    void truncateTable();
    Stock loadStock(String stockTag);
    List<Stock> loadAllStocks();
}
