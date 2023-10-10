package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.service.IntraDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/intraday/")
public class IntraDayController {

    private final IntraDayService intraDayService;

    @Autowired
    public IntraDayController(IntraDayService intraDayService) {
        this.intraDayService = intraDayService;
    }

    @PutMapping(path = "save")
    public void saveIntraDay() {
        intraDayService.saveIntraDay();
    }
}
