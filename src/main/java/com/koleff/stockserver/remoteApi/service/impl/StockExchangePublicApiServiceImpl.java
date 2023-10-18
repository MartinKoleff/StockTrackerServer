package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.service.impl.StockExchangeServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.StockExchangeJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StockExchangePublicApiServiceImpl extends PublicApiServiceImpl<StockExchange> {

    private final StockExchangeServiceImpl stockExchangeServiceImpl;

    @Autowired
    public StockExchangePublicApiServiceImpl(StockServiceImpl stockServiceImpl, StockExchangeJsonUtil jsonUtil, StockExchangeServiceImpl stockExchangeServiceImpl) {
        super(stockServiceImpl, jsonUtil);
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
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
    }
}
