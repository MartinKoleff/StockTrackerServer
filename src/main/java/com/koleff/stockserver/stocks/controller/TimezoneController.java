package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.dto.TimezoneDto;
import com.koleff.stockserver.stocks.service.impl.TimezoneServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "api/v1/timezone/")
public class TimezoneController {

    private final TimezoneServiceImpl timezoneServiceImpl;

    @Autowired
    public TimezoneController(TimezoneServiceImpl timezoneServiceImpl) {
        this.timezoneServiceImpl = timezoneServiceImpl;
    }

    @GetMapping(path = "get/all")
    public List<TimezoneDto> getTimezones() {
        return timezoneServiceImpl.getTimezones();
    }

    @GetMapping(path = "get/{timezone_id}")
    public TimezoneDto getTimezone(@PathVariable("timezone_id") Long id) {
        return timezoneServiceImpl.getTimezone(id);
    }

    @GetMapping(path = "get/{stock_tag}")
    public TimezoneDto getTimezone(@PathVariable("stock_tag") String stockTag) {
        return timezoneServiceImpl.getTimezone(stockTag);
    }

    /**
     * Save bulk to DB
     */
    @PutMapping(path = "save/all")
    public void saveCurrencies(@Valid List<Timezone> timezones) {
        timezoneServiceImpl.saveTimezones(timezones);
    }

    /**
     * Save to DB
     */
    @PutMapping(path = "save")
    public void saveTimezone(@Valid Timezone timezone) {
        timezoneServiceImpl.saveTimezone(timezone);
    }

    /**
     * Load all from JSON
     */
    @GetMapping(path = "load/all")
    public List<Timezone> loadAllTimezones() {
        return timezoneServiceImpl.loadAllTimezones();
    }

    /**
     * Load from JSON
     */
    @PutMapping(path = "load/{timezone}")
    public Timezone loadTimezone(@PathVariable("timezone") String timezone) {
        return timezoneServiceImpl.loadTimezone(timezone);
    }

     /**
      * Load from JSON
      */
//    @PutMapping(path = "load/{stock_tag}")
//    public Currency loadCurrency(@PathVariable("stock_tag") String stockTag) {
//        return currencyServiceImpl.loadCurrency(stockTag);
//    }
}
