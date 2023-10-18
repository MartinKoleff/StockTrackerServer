package com.koleff.stockserver.stocks.domain.wrapper;

import com.google.gson.annotations.SerializedName;
import com.koleff.stockserver.stocks.domain.StockExchange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class StockWithStockExchange {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("tag")
    private String tag;

    @SerializedName("has_intraday")
    private Boolean hasIntraDay;

    @SerializedName("has_end_of_day")
    private Boolean hasEndOfDay;

    @SerializedName("stock_exchange_id")
    private Long stockExchangeId;

    @SerializedName("stock_exchange")
    private StockExchange stockExchange;
}

