package com.koleff.stockserver.stocks.utils.jsonUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class EndOfDayJsonUtil extends JsonUtil<DataWrapper<EndOfDay>> {
    public EndOfDayJsonUtil(Gson gson) {
        super(gson);
    }

    @Override
    public Type getType() {
        return new TypeToken<DataWrapper<EndOfDay>>() {}.getType();
    }

    @Override
    protected String getDirectory() {
        return "eod";
    }
}
