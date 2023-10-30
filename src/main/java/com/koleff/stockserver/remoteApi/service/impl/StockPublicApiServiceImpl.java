package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.client.v2.StockPublicApiClientV2;
import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.tickersUtil.TickersUtil;
import com.koleff.stockserver.stocks.utils.jsonUtil.StockJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StockPublicApiServiceImpl extends PublicApiServiceImpl<Stock> {

    private final StockServiceImpl stockServiceImpl;
    private final StockPublicApiClientV2 stockPublicApiClientV2;
    private final TickersUtil tickersUtil;
    @Autowired
    public StockPublicApiServiceImpl(StockServiceImpl stockServiceImpl,
                                     StockJsonUtil jsonUtil,
                                     StockPublicApiClientV2 stockPublicApiClientV2,
                                     TickersUtil tickersUtil) {
        super(stockServiceImpl, stockPublicApiClientV2, jsonUtil);
        this.stockServiceImpl = stockServiceImpl;
        this.stockPublicApiClientV2 = stockPublicApiClientV2;
        this.tickersUtil = tickersUtil;
    }


    @Override
    public String getRequestName() {
        return "tickers";
    }

    @Override
    protected void saveToRepository(List<Stock> data) {
        stockServiceImpl.saveStocks(data);
    }

    @Override
    protected void configureJoin(List<Stock> data, String stockTag) {
        //Configure stock_exchange_id
        tickersUtil.configureIds();
    }
}
