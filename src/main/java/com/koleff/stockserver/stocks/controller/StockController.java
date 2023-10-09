package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/stock/")
public class StockController {

    @Autowired
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

    @PutMapping(path = "save")
    public void saveStocks(){
        stockService.saveStocks();
    }
}
