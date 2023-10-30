package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.client.v2.StockExchangePublicApiClientV2;
import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.service.impl.StockExchangeServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.StockExchangeJsonUtil;
import com.koleff.stockserver.stocks.utils.stockExchangesUtil.StockExchangesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StockExchangePublicApiServiceImpl extends PublicApiServiceImpl<StockExchange> {
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final StockExchangePublicApiClientV2 stockExchangePublicApiClientV2;
    private final StockExchangesUtil stockExchangesUtil;

    @Autowired
    public StockExchangePublicApiServiceImpl(StockServiceImpl stockServiceImpl,
                                             StockExchangeJsonUtil jsonUtil,
                                             StockExchangeServiceImpl stockExchangeServiceImpl,
                                             StockExchangePublicApiClientV2 stockExchangePublicApiClientV2,
                                             StockExchangesUtil stockExchangesUtil) {
        super(stockServiceImpl, stockExchangePublicApiClientV2, jsonUtil);
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.stockExchangePublicApiClientV2 = stockExchangePublicApiClientV2;
        this.stockExchangesUtil = stockExchangesUtil;
    }


    @Override
    public String getRequestName() {
        return "exchange";
    }

    @Override
    protected void saveToRepository(List<StockExchange> data) {
        stockExchangeServiceImpl.saveStockExchanges(data);
    }

    @Override
    protected void configureJoin(List<StockExchange> data, String stockTag) {
        stockExchangesUtil.configureIds();
    }
}
