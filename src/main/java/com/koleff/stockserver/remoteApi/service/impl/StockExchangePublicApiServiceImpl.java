package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.StockExchangeJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StockExchangePublicApiServiceImpl extends PublicApiServiceImpl<StockExchange> {

    @Autowired
    public StockExchangePublicApiServiceImpl(StockServiceImpl stockServiceImpl, StockExchangeJsonUtil jsonUtil) {
        super(stockServiceImpl, jsonUtil);
    }


    @Override
    public String getRequestName() {
        return "exchange";
    }
}
