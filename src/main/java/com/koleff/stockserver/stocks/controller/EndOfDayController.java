package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.service.impl.EndOfDayServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/eod/")
public class EndOfDayController {

    private final EndOfDayServiceImpl endOfDayServiceImpl;

    @Autowired
    public EndOfDayController(EndOfDayServiceImpl endOfDayServiceImpl) {
        this.endOfDayServiceImpl = endOfDayServiceImpl;
    }

    @GetMapping("get/all")
    public List<List<EndOfDayDto>> getAllEndOfDays() {
        return endOfDayServiceImpl.getAllEndOfDays();
    }

    @GetMapping("get/{stock_id}")
    public List<EndOfDayDto> getEndOfDay(@PathVariable("stock_id") Long stockId) {
        return endOfDayServiceImpl.getEndOfDay(stockId);
    }

    @GetMapping("get/{stock_tag}")
    public List<EndOfDayDto> getEndOfDay(@PathVariable("stock_tag") String stockTag) {
        return endOfDayServiceImpl.getEndOfDay(stockTag);
    }

    /**
     * Save bulk to DB
     */
    @PutMapping("save/all")
    public void saveAllEndOfDays(@Valid List<List<EndOfDay>> endOfDays) {
        endOfDayServiceImpl.saveAllEndOfDays(endOfDays);
    }

    /**
     * Save to DB
     */
    @PutMapping("save")
    public void saveEndOfDay(@Valid List<EndOfDay> endOfDay) {
        endOfDayServiceImpl.saveEndOfDay(endOfDay);
    }

    /**
     * Load all from JSON
     */
    @GetMapping("load/all")
    public List<List<EndOfDay>> loadAllEndOfDays() {
        return endOfDayServiceImpl.loadAllEndOfDays();
    }

    /**
     * Load from JSON
     */
    @GetMapping("load/{stock_tag}")
    public List<EndOfDay> loadEndOfDay(@PathVariable("stock_tag") String stockTag) {
        return endOfDayServiceImpl.loadEndOfDay(stockTag);
    }
}
