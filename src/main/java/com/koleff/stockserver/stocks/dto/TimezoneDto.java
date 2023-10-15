package com.koleff.stockserver.stocks.dto;

import com.google.gson.annotations.SerializedName;

public record TimezoneDto(
    @SerializedName("timezone")
    String timezone,
    @SerializedName("abbreviation")
    String abbreviation,
    @SerializedName("abbreviation_dst")
    String abbreviationDst
){

}
