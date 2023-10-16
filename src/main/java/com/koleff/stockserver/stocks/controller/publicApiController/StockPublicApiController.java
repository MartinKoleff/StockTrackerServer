package com.koleff.stockserver.stocks.controller.publicApiController;

import com.koleff.stockserver.stocks.client.v2.StockExchangePublicApiClientV2;
import com.koleff.stockserver.stocks.client.v2.StockPublicApiClientV2;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import com.koleff.stockserver.stocks.service.impl.PublicApiServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("publicApi/v1/tickers")
public class StockPublicApiController extends PublicApiController<Stock> {

    private final DatabaseTableDto databaseTableDto = new DatabaseTableDto("tickers");

    public StockPublicApiController(PublicApiServiceImpl<Stock> publicApiServiceImpl,
                                    StockPublicApiClientV2 stockPublicApiClientV2) {
        super(publicApiServiceImpl, stockPublicApiClientV2);
    }

    /**
     * Get from remote API
     */
    @Override
    @GetMapping("get/{stockTag}")
    public DataWrapper<Stock> getData(@PathVariable("stockTag") String stockTag) {
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
