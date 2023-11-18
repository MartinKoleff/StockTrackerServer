package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "api/v1/app/")
public class AppController {

    CurrencyServiceImpl currencyServiceImpl;
    TimezoneServiceImpl timezoneServiceImpl;
    StockExchangeServiceImpl stockExchangeServiceImpl;
    StockServiceImpl stockServiceImpl;
    EndOfDayServiceImpl endOfDayServiceImpl;
    IntraDayServiceImpl intraDayServiceImpl;

    @Autowired
    public AppController(CurrencyServiceImpl currencyServiceImpl,
                         TimezoneServiceImpl timezoneServiceImpl,
                         StockExchangeServiceImpl stockExchangeServiceImpl,
                         StockServiceImpl stockServiceImpl,
                         EndOfDayServiceImpl endOfDayServiceImpl,
                         IntraDayServiceImpl intraDayServiceImpl) {
        this.currencyServiceImpl = currencyServiceImpl;
        this.timezoneServiceImpl = timezoneServiceImpl;
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.endOfDayServiceImpl = endOfDayServiceImpl;
        this.intraDayServiceImpl = intraDayServiceImpl;
    }

    @PostMapping("load&save/all")
    void loadAndSaveAllData(){
        currencyServiceImpl.loadAndSaveAllCurrencies();
        timezoneServiceImpl.loadAndSaveAllTimezones();
        stockExchangeServiceImpl.loadAndSaveAllStockExchanges();
        stockServiceImpl.loadAndSaveAllStocks();
        endOfDayServiceImpl.saveViaJob();
        intraDayServiceImpl.saveViaJob();
    }

    @PostMapping("delete/all")
    void clearAllTables(){ //TODO: add query -> SELECT setval('currency_sequence', 1, false);
        currencyServiceImpl.truncateTable();
        timezoneServiceImpl.truncateTable();
        stockExchangeServiceImpl.truncateTable();
        stockServiceImpl.truncateTable();
        endOfDayServiceImpl.truncateTable();
        intraDayServiceImpl.truncateTable();
    }
}
