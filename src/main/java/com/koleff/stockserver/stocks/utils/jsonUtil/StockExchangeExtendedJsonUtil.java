package com.koleff.stockserver.stocks.utils.jsonUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.domain.wrapper.StockExchangeExtended;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class StockExchangeExtendedJsonUtil extends JsonUtil<DataWrapper<StockExchangeExtended>> {
    public StockExchangeExtendedJsonUtil(Gson gson) {
        super(gson);
    }

    @Override
    public Type getType() {
        return new TypeToken<DataWrapper<StockExchangeExtended>>() {}.getType();
    }

    @Override
    protected String getDirectory() {
        return "exchanges";
    }
}
