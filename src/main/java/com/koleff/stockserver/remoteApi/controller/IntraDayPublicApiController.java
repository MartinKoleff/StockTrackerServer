package com.koleff.stockserver.remoteApi.controller;

import com.koleff.stockserver.remoteApi.client.v2.IntraDayPublicApiClientV2;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.remoteApi.service.impl.PublicApiServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("publicApi/v1/intraday")
public class IntraDayPublicApiController extends PublicApiController<IntraDay> {

    private final DatabaseTableDto databaseTableDto = new DatabaseTableDto("intraday");

    public IntraDayPublicApiController(PublicApiServiceImpl<IntraDay> publicApiServiceImpl,
                                       IntraDayPublicApiClientV2 intraDayPublicApiClientV2) {
        super(publicApiServiceImpl, intraDayPublicApiClientV2);
    }

    /**
     * Get from remote API
     */
    @Override
    @GetMapping("get/{stockTag}")
    public DataWrapper<IntraDay> getData(@PathVariable("stockTag") String stockTag) {
        return super.getData(stockTag);
    }

    /**
     * Get from remote API and export to JSON
     */
    @GetMapping("export/{stockTag}")
    public void exportDataToJson(@PathVariable("stockTag") String stockTag) {
        super.exportDataToJson(databaseTableDto, stockTag);
    }

    /**
     * Save to DB
     */
    @PutMapping("save/{stockTag}") //TODO: add data as dependency
    public void saveData(@PathVariable("stockTag") String stockTag) {
        super.saveData(databaseTableDto, stockTag);
    }

    /**
     * Save all to DB
     */
    @PutMapping("save/all") //TODO: add data as dependency
    public void saveBulkData() {
        super.saveBulkData(databaseTableDto);
    }

    /**
     * Load from JSON
     */
    @GetMapping("load/{stockTag}")
    public void loadData(@PathVariable("stockTag") String stockTag) {
        super.loadData(databaseTableDto, stockTag);
    }

    /**
     * Load all from JSON
     */
    @GetMapping("load/all")
    public void loadBulkData() {
        super.loadBulkData(databaseTableDto);
    }
}
