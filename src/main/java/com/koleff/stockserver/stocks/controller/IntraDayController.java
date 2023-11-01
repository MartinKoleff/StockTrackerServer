package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.service.impl.IntraDayServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/intraday/")
public class IntraDayController {

    private final IntraDayServiceImpl intraDayServiceImpl;

    @Autowired
    public IntraDayController(IntraDayServiceImpl intraDayServiceImpl) {
        this.intraDayServiceImpl = intraDayServiceImpl;
    }

    @GetMapping("get/all")
    public List<List<IntraDayDto>> getAllIntraDays() {
        return intraDayServiceImpl.getAllIntraDays();
    }

    @GetMapping("get/{stock_id}")
    public List<IntraDayDto> getIntraDays(@PathVariable("stock_id") Long stockId) {
        return intraDayServiceImpl.getIntraDays(stockId);
    }

    @GetMapping("get/{stock_tag}")
    public List<IntraDayDto> getIntraDays(@PathVariable("stock_tag") String stockTag) {
        return intraDayServiceImpl.getIntraDays(stockTag);
    }

    @GetMapping("get/{stock_tag}")
    public List<IntraDayDto> getIntraDays(@PathVariable("stock_tag") String stockTag, String dateFrom, String dateTo) {
        return intraDayServiceImpl.getIntraDays(stockTag, dateFrom, dateTo);
    }

    @GetMapping("get/{stock_tag}")
    public IntraDayDto getIntraDay(@PathVariable("stock_tag") String stockTag, String date) {
        return intraDayServiceImpl.getIntraDay(stockTag, date);
    }

    /**
     * Save bulk to DB
     */
    @PutMapping("save/all")
    public void saveAllIntraDays(@Valid List<List<IntraDay>> intraDay) {
        intraDayServiceImpl.saveAllIntraDays(intraDay);
    }

    /**
     * Save to DB
     */
    @PutMapping("save")
    public void saveIntraDay(@Valid List<IntraDay> intraDay) {
        intraDayServiceImpl.saveIntraDay(intraDay);
    }

    /**
     * Load all from JSON
     */
    @GetMapping("load/all")
    public List<List<IntraDay>> loadAllIntraDays() {
        return intraDayServiceImpl.loadAllIntraDays();
    }

    /**
     * Load from JSON
     */
    @GetMapping("load/{stock_tag}")
    public List<IntraDay> loadIntraDays(@PathVariable("stock_tag") String stockTag) {
        return intraDayServiceImpl.loadIntraDays(stockTag);
    }
}
