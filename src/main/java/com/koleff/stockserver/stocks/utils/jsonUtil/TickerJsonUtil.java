package com.koleff.stockserver.stocks.utils.jsonUtil;

import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.Ticker;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class TickerJsonUtil extends JsonUtil<DataWrapper<Ticker>> {
    @Override
    public Type getType() {
        return new TypeToken<DataWrapper<Ticker>>() {}.getType();
    }
}
