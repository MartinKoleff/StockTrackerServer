package com.koleff.stockserver.stocks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.util.List;

public record StockDto(
        @JsonProperty("id")
//        @JsonIgnore()
        Long id,
        @JsonProperty("name")
        String name,
        @JsonProperty("tag")
        String tag,
        @JsonProperty("country")
        String country,
        @JsonProperty("has_intra_day")
        Boolean hasIntraDay,
        @JsonProperty("has_end_of_day")
        Boolean hasEndOfDay,
        @JsonProperty("end_of_day")
        List<EndOfDayDto> endOfDayDtosList,
        @JsonProperty("intra_day")
        List<IntraDayDto> intraDayDtosList,
        @JsonProperty("stock_exchange")
        List<StockExchangeDto> stockExchangeDtosList
) {

    public String toJson() {
        return new Gson().toJson(this);
    }
}
