package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.service.impl.EndOfDayServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.EndOfDayJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EndOfDayPublicApiServiceImpl extends PublicApiServiceImpl<EndOfDay> {

    private final EndOfDayServiceImpl endOfDayServiceImpl;
    private final StockServiceImpl stockServiceImpl;


    @Autowired
    public EndOfDayPublicApiServiceImpl(StockServiceImpl stockServiceImpl,
                                        EndOfDayJsonUtil jsonUtil,
                                        EndOfDayServiceImpl endOfDayServiceImpl,
                                        StockServiceImpl stockServiceImpl1) {
        super(stockServiceImpl, jsonUtil);
        this.endOfDayServiceImpl = endOfDayServiceImpl;
        this.stockServiceImpl = stockServiceImpl1;
    }


    @Override
    public String getRequestName() {
        return "eod";
    }

    @Override
    protected void saveToRepository(List<EndOfDay> data) {
        endOfDayServiceImpl.saveEndOfDay(data);
    }

    @Override
    protected void configureJoin(List<EndOfDay> data, String stockTag) {
        //Configure stock_id
        data.forEach(entity ->
                entity.setStockId(
                        stockServiceImpl.getStockId(stockTag)
                )
        );
    }
}
