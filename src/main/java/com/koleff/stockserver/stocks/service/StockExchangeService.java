package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;

import java.util.List;

public interface StockExchangeService {
    StockExchangeDto getStockExchange(Long id);
    List<StockExchangeDto> getStockExchange(String country);
    List<StockExchangeDto> getStockExchanges();
    void saveStockExchange(StockExchange stockExchange);
    void saveStockExchanges(List<StockExchange> data);
    void deleteById(Long id);
    void deleteAll();
    // TODO: loadStockExchange()
    List<StockExchange> loadAllStockExchanges();
}
