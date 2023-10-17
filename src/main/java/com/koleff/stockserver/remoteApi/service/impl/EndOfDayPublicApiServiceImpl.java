package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.EndOfDayJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EndOfDayPublicApiServiceImpl extends PublicApiServiceImpl<EndOfDay> {

    @Autowired
    public EndOfDayPublicApiServiceImpl(StockServiceImpl stockServiceImpl, EndOfDayJsonUtil jsonUtil) {
        super(stockServiceImpl, jsonUtil);
    }


    @Override
    public String getRequestName() {
        return "eod";
    }
}
