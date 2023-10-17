package com.koleff.stockserver.stocks.utils.jsonUtil;

import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class StockExchangeJsonUtil extends JsonUtil<DataWrapper<StockExchange>> {
    @Override
    public Type getType() {
        return new TypeToken<DataWrapper<StockExchange>>() {}.getType();
    }
}
