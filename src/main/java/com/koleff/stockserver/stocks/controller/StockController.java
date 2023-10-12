package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/stock/")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping(path = "get/all")
    public List<StockDto> getStocks() {
        return stockService.getStocks();
    }

    @GetMapping(path = "get/{stock_id}")
    public StockDto getStock(@PathVariable("stock_id") Long id) {
        return stockService.getStock(id);
    }

    @GetMapping(path = "get/{stock_tag}")
    public StockDto getStock(@PathVariable("stock_tag") String stockTag) {
        return stockService.getStock(stockTag);
    }

    /**
     * Save bulk to DB
     */
    @PutMapping(path = "save/all")
    public void saveStocks(@Valid List<Stock> stocks) {
        stockService.saveStocks(stocks);
    }

    /**
     * Save to DB
     */
    @PutMapping(path = "save")
    public void saveStock(@Valid Stock stock) {
        stockService.saveStock(stock);
    }

    /**
     * Load all from JSON
     */
    @GetMapping(path = "load/all")
    public List<Stock> loadStocks() {
        return stockService.loadStocks();
    }

    /**
     * Load from JSON
     */
    @PutMapping(path = "load/{stockTag}")
    public Stock loadStock(@PathVariable("stockTag") String stockTag) {
        return stockService.loadStock(stockTag);
    }
}
