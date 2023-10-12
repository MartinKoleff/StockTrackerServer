package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.service.IntraDayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/intraday/")
public class IntraDayController {

    private final IntraDayService intraDayService;

    @Autowired
    public IntraDayController(IntraDayService intraDayService) {
        this.intraDayService = intraDayService;
    }

    @GetMapping("get/all")
    public List<List<IntraDayDto>> getAllIntraDays() {
        return intraDayService.getAllIntraDays();
    }

    @GetMapping("get/{stock_id}")
    public List<IntraDayDto> getIntraDay(@PathVariable("stock_id") Long stockId) {
        return intraDayService.getIntraDay(stockId);
    }

    @GetMapping("get/{stock_tag}")
    public List<IntraDayDto> getIntraDay(@PathVariable("stock_tag") String stockTag) {
        return intraDayService.getIntraDay(stockTag);
    }

    /**
     * Save bulk to DB
     */
    @PutMapping("save/all")
    public void saveAllIntraDay(@Valid List<List<IntraDay>> intraDay) {
        intraDayService.saveAllIntraDay(intraDay);
    }

    /**
     * Save to DB
     */
    @PutMapping("save")
    public void saveIntraDay(@Valid List<IntraDay> intraDay) {
        intraDayService.saveIntraDay(intraDay);
    }

    /**
     * Load all from JSON
     */
    @GetMapping("load/all")
    public List<List<IntraDay>> loadAllIntraDays() {
        return intraDayService.loadAllIntraDays();
    }

    /**
     * Load from JSON
     */
    @GetMapping("load/{stock_tag}")
    public List<IntraDay> loadIntraDay(@PathVariable("stock_tag") String stockTag) {
        return intraDayService.loadIntraDay(stockTag);
    }
}
