package com.koleff.stockserver.stocks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.koleff.stockserver.stocks.domain.Stock;

import java.sql.Date;

public record EndOfDayDto(
        @JsonProperty("id")
        Long id,
        @JsonProperty("stock_id")
        Long stockId,
        @JsonProperty("open")
        Double open,
        @JsonProperty("close")
        Double close,
        @JsonProperty("high")
        Double high,
        @JsonProperty("low")
        Double low,
        @JsonProperty("volume")
        Double volume,
        @JsonProperty("adj_open")
        Double adjustOpen,
        @JsonProperty("adj_close")
        Double adjustClose,
        @JsonProperty("adj_high")
        Double adjustHigh,
        @JsonProperty("adj_low")
        Double adjustLow,
        @JsonProperty("adj_volume")
        Double adjustVolume,
        @JsonProperty("split_factor")
        Double splitFactor,
        @JsonProperty("date")
        Date date
//        @JsonProperty("stock")
//        @JsonIgnore
//        Stock stock
) {
}
