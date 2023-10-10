package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.service.StockService;
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

    @GetMapping(path = "stocks")
    public List<StockDto> getStocks() {
        return stockService.getStocks();
    }

    @GetMapping(path = "{stockId}")
    public StockDto getStock(@PathVariable("stockId") Long id) {
        return stockService.getStock(id);
    }

    @PutMapping(path = "save/{stockTag}")
    public void saveStock(@PathVariable("stockTag") String stockTag){
        stockService.saveStock(stockTag);
    }

    @PutMapping(path = "save/all")
    public void saveStocks(){
        stockService.saveStocks();
    }
}
