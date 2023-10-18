package com.koleff.stockserver.remoteApi.service.impl;

import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.service.impl.IntraDayServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.jsonUtil.IntraDayJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class IntraDayPublicApiServiceImpl extends PublicApiServiceImpl<IntraDay> {

    private final IntraDayServiceImpl intraDayServiceImpl;
    private final StockServiceImpl stockServiceImpl;

    @Autowired
    public IntraDayPublicApiServiceImpl(StockServiceImpl stockServiceImpl,
                                        IntraDayJsonUtil jsonUtil,
                                        IntraDayServiceImpl intraDayServiceImpl) {
        super(stockServiceImpl, jsonUtil);
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.stockServiceImpl = stockServiceImpl;
    }


    @Override
    public String getRequestName() {
        return "intraday";
    }

    @Override
    protected void saveToRepository(List<IntraDay> data) {
        intraDayServiceImpl.saveIntraDay(data);
    }

    @Override
    protected void configureJoin(List<IntraDay> data, String stockTag) {
        //Configure stock_id
        data.forEach(entity ->
                entity.setStockId(
                        stockServiceImpl.getStockId(stockTag)
                )
        );
    }
}
