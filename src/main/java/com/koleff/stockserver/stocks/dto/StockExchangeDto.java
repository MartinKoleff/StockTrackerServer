package com.koleff.stockserver.stocks.dto;

import com.google.gson.annotations.SerializedName;

public record StockExchangeDto (
    @SerializedName("name")
    String name,

    @SerializedName("acronym")
    String acronym,
    @SerializedName("country")
    String country,

    @SerializedName("country_code")
    String countryCode,

    @SerializedName("city")
    String city,

    @SerializedName("website")
    String website,

    @SerializedName("timezone")
    TimezoneDto timezoneDto,

    @SerializedName("currency")
    CurrencyDto currencyDto
){

}
