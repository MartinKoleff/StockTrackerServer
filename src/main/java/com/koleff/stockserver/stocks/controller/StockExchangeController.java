package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import com.koleff.stockserver.stocks.service.StockExchangeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "api/v1/stock_exchange/")
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @Autowired
    public StockExchangeController(StockExchangeService stockExchangeService) {
        this.stockExchangeService = stockExchangeService;
    }

    @GetMapping(path = "get/all")
    public List<StockExchangeDto> getStockExchanges() {
        return stockExchangeService.getStockExchanges();
    }

    @GetMapping(path = "get/{stock_exchange_id}")
    public StockExchangeDto getStockExchange(@PathVariable("stock_exchange_id") Long id) {
        return stockExchangeService.getStockExchange(id);
    }

    @GetMapping(path = "get/{country}")
    public StockExchangeDto getStock(@PathVariable("country") String country) {
        return stockExchangeService.getStockExchange(country);
    }


    /**
     * Save bulk to DB
     */
    @PutMapping(path = "save/all")
    public void saveStockExchanges(@Valid List<StockExchange> stockExchanges) {
        stockExchangeService.saveStockExchanges(stockExchanges);
    }

    /**
     * Save to DB
     */
    @PutMapping(path = "save")
    public void saveStockExchange(@Valid StockExchange stockExchange) {
        stockExchangeService.saveStockExchange(stockExchange);
    }

    /**
     * Load all from JSON
     */
    @GetMapping(path = "load/all")
    public List<StockExchange> loadStockExchanges() {
        return stockExchangeService.loadStockExchanges();
    }
}
