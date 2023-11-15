package com.koleff.stockserver.stocks.controller;

import com.koleff.stockserver.stocks.service.ExceptionTesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/test/exception/")
public class ExceptionTesterController {

    private ExceptionTesterService exceptionTesterService;

    @Autowired
    public ExceptionTesterController(ExceptionTesterService exceptionTesterService) {
        this.exceptionTesterService = exceptionTesterService;
    }


    /**
     * Used for testing only
     * Throw exception based on endpoint name
     */
    @GetMapping("{exceptionName}")
    public void getError(@PathVariable("exceptionName") String exceptionName) throws RuntimeException {
        exceptionTesterService.getError(exceptionName);
    }
}
