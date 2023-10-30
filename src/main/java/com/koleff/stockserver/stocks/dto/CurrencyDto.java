package com.koleff.stockserver.stocks.dto;

import com.google.gson.annotations.SerializedName;

public record CurrencyDto(
    @SerializedName("code")
    String code,
    @SerializedName("symbol")
    String symbol,
    @SerializedName("name")
    String name
){

}
