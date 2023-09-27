package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.Ticker;
import com.koleff.stockserver.stocks.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class TickerController {

    private final TickerService tickerService;

    @Autowired
    public TickerController(TickerService tickerService) {
        this.tickerService = tickerService;
    }

    @GetMapping(path = "tickers")
    public List<Ticker> getTickers() {
        return tickerService.getTickers();
    }

    @GetMapping(path = "{tickerId}")
    public Ticker getTicker(@PathVariable("tickerId") Long id) {
        return tickerService.getTicker(id);
    }

}
