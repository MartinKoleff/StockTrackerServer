package com.koleff.stockserver.remoteApi.controller;

import com.koleff.stockserver.remoteApi.client.v2.EndOfDayPublicApiClientV2;
import com.koleff.stockserver.remoteApi.controller.base.PublicApiController;
import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("publicApi/v1/eod/")
public class EndOfDayPublicApiController extends PublicApiController<EndOfDay> {

    @Autowired
    public EndOfDayPublicApiController(PublicApiServiceImpl<EndOfDay> publicApiServiceImpl) {
        super(publicApiServiceImpl);
    }

    /**
     * Get from remote API
     */
    @Override
    @GetMapping("get/{stockTag}")
    public DataWrapper<EndOfDay> getData(@PathVariable("stockTag") String stockTag) {
        return super.getData(stockTag);
    }

    /**
     * Get from remote API and export to JSON
     */
    @Override
    @GetMapping("export/{stockTag}")
    public void exportDataToJson(@PathVariable("stockTag") String stockTag) {
        super.exportDataToJson(stockTag);
    }

    /**
     * Save to DB
     */
    @Override
    @PutMapping("save/{stockTag}") //TODO: add data as dependency
    public void saveData(@PathVariable("stockTag") String stockTag) {
        super.saveData(stockTag);
    }

    /**
     * Save all to DB
     */
    @Override
    @PutMapping("save/all") //TODO: add data as dependency
    public void saveBulkData() {
        super.saveBulkData();
    }

    /**
     * Load from JSON
     */
    @Override
    @GetMapping("load/{stockTag}")
    public void loadData(@PathVariable("stockTag") String stockTag) {
        super.loadData(stockTag);
    }

    /**
     * Load all from JSON
     */
    @Override
    @GetMapping("load/all")
    public void loadBulkData() {
        super.loadBulkData();
    }
}
