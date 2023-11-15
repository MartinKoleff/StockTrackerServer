package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.dto.CurrencyDto;
import com.koleff.stockserver.stocks.service.impl.CurrencyServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "api/v1/currency/")
public class CurrencyController {

    private final CurrencyServiceImpl currencyServiceImpl;

    @Autowired
    public CurrencyController(CurrencyServiceImpl currencyServiceImpl) {
        this.currencyServiceImpl = currencyServiceImpl;
    }

    @GetMapping(path = "get/all")
    public List<CurrencyDto> getCurrencies() {
        return currencyServiceImpl.getCurrencies();
    }

    @GetMapping(path = "get/{currency_id}")
    public CurrencyDto getCurrency(@PathVariable("currency_id") Long id) {
        return currencyServiceImpl.getCurrency(id);
    }

    @GetMapping(path = "get/{stock_tag}")
    public CurrencyDto getCurrency(@PathVariable("stock_tag") String stockTag) {
        return currencyServiceImpl.getCurrency(stockTag);
    }

    /**
     * Save bulk to DB
     */
    @PutMapping(path = "save/all")
    public void saveCurrencies(@Valid List<Currency> currencies) {
        currencyServiceImpl.saveCurrencies(currencies);
    }

    /**
     * Load and save bulk to DB
     */
    @PostMapping("load&save/all")
    public void loadAndSaveAllCurrencies() {
        currencyServiceImpl.loadAndSaveAllCurrencies();
    }

    /**
     * Save to DB
     */
    @PutMapping(path = "save")
    public void saveCurrency(@Valid Currency currency) {
        currencyServiceImpl.saveCurrency(currency);
    }

    /**
     * Load all from JSON
     */
    @GetMapping(path = "load/all")
    public List<Currency> loadAllCurrencies() {
        return currencyServiceImpl.loadAllCurrencies();
    }

    /**
     * Load from JSON
     */
    @PutMapping(path = "load/{currency_code}")
    public Currency loadCurrency(@PathVariable("currency_code") String currencyCode) {
        return currencyServiceImpl.loadCurrency(currencyCode);
    }

     /**
      * Load from JSON
      */
//    @PutMapping(path = "load/{stock_tag}")
//    public Currency loadCurrency(@PathVariable("stock_tag") String stockTag) {
//        return currencyServiceImpl.loadCurrency(stockTag);
//    }
}
