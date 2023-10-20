package com.koleff.stockserver.stocks.utils.jsonUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class TimezoneJsonUtil extends JsonUtil<DataWrapper<Timezone>> {
    public TimezoneJsonUtil(Gson gson) {
        super(gson);
    }

    @Override
    public Type getType() {
        return new TypeToken<DataWrapper<Timezone>>() {}.getType();
    }
}
