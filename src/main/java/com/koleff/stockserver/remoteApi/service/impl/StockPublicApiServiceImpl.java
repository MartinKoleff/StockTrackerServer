package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.StockJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StockPublicApiServiceImpl extends PublicApiServiceImpl<Stock> {

    @Autowired
    public StockPublicApiServiceImpl(StockServiceImpl stockServiceImpl, StockJsonUtil jsonUtil) {
        super(stockServiceImpl, jsonUtil);
    }


    @Override
    public String getRequestName() {
        return "tickers";
    }
}
