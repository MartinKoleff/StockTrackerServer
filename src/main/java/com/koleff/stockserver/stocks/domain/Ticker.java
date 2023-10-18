package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Ticker {
    @SerializedName("stock_exchange_id")
    private Long stockExchangeId;

    @SerializedName("name")
    private String name;

    @SerializedName("tag")
    private String tag;

    @SerializedName("has_intraday")
    private Boolean hasIntraDay;

    @SerializedName("has_end_of_day")
    private Boolean hasEndOfDay;
}
