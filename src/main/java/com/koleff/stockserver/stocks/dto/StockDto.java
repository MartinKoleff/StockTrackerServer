package com.koleff.stockserver.stocks.dto;

import com.google.gson.Gson;

public record StockDto(
        Long id,
        String name,
        String tag,
        String country,
        Boolean hasIntraDay,
        Boolean hasEndOfDay
) {

    //ADD...
    //endOfDay
    //intraDay
    //stockExchange


    public String toJson() {
        return new Gson().toJson(this);
    }

}
