package com.koleff.stockserver.stocks.utils.jsonUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class CurrencyJsonUtil extends JsonUtil<DataWrapper<Currency>> {
    public CurrencyJsonUtil(Gson gson) {
        super(gson);
    }

    @Override
    public Type getType() {
        return new TypeToken<DataWrapper<Currency>>() {}.getType();
    }

    @Override
    protected String getDirectory() {
        return "currencies";
    }
}
