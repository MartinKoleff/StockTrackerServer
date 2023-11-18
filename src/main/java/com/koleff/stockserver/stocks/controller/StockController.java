package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/stock/")
public class StockController {

    private final StockServiceImpl stockServiceImpl;

    @Autowired
    public StockController(StockServiceImpl stockServiceImpl) {
        this.stockServiceImpl = stockServiceImpl;
    }

    @GetMapping(path = "get/all")
    public List<StockDto> getStocks() {
        return stockServiceImpl.getStocks();
    }

    @GetMapping(path = "get/{stock_id}")
    public StockDto getStock(@PathVariable("stock_id") Long id) {
        return stockServiceImpl.getStock(id);
    }

    @GetMapping(path = "get/{stock_tag}")
    public StockDto getStock(@PathVariable("stock_tag") String stockTag) {
        return stockServiceImpl.getStockDto(stockTag);
    }

    /**
     * Save bulk to DB
     */
    @PutMapping(path = "save/all")
    public void saveStocks(@Valid List<Stock> stocks) {
        stockServiceImpl.saveStocks(stocks);
    }

    /**
     * Save to DB
     */
    @PutMapping(path = "save")
    public void saveStock(@Valid Stock stock) {
        stockServiceImpl.saveStock(stock);
    }

    /**
     * Load and save bulk to DB
     */
    @PostMapping("load&save/all")
    public void loadAndSaveAllStocks() {
        stockServiceImpl.loadAndSaveAllStocks();
    }

    /**
     * Load all from JSON
     */
    @GetMapping(path = "load/all")
    public List<Stock> loadAllStocks() {
        return stockServiceImpl.loadAllStocks();
    }

    /**
     * Load from JSON
     */
    @PutMapping(path = "load/{stock_tag}")
    public Stock loadStock(@PathVariable("stock_tag") String stockTag) {
        return stockServiceImpl.loadStock(stockTag);
    }
}
