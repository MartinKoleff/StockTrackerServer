package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.service.IntraDayService;
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

    @PutMapping("save")
    public void saveIntraDay() {
        intraDayService.saveIntraDay();
    }

    @GetMapping("{stock_tag}")
    public List<IntraDayDto> getIntraDay(@PathVariable("stock_tag") String stockTag){
       return intraDayService.getIntraDay(stockTag);
    }

    @GetMapping("{stock_id}")
    public List<IntraDayDto> getIntraDay(@PathVariable("stock_id") Long stockId){
        return intraDayService.getIntraDay(stockId);
    }
}
