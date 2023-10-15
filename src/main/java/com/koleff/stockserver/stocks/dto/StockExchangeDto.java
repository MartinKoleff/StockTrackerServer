package com.koleff.stockserver.stocks.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public record StockExchangeDto (
    @SerializedName("name")
    String name,

    @SerializedName("acronym")
    String acronym,

    @SerializedName("exchange")
    String exchange,

    @SerializedName("country")
    String country,

    @SerializedName("country_code")
    String countryCode,

    @SerializedName("city")
    String city,

    @SerializedName("website")
    String website
){

}
