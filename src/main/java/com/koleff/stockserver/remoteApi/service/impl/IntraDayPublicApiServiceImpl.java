package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.IntraDayJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IntraDayPublicApiServiceImpl extends PublicApiServiceImpl<IntraDay> {

    @Autowired
    public IntraDayPublicApiServiceImpl(StockServiceImpl stockServiceImpl, IntraDayJsonUtil jsonUtil) {
        super(stockServiceImpl, jsonUtil);
    }


    @Override
    public String getRequestName() {
        return "intraday";
    }
}
