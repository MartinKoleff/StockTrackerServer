package com.koleff.stockserver.stocks.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public record IntraDayDto(
        @SerializedName("open")
        Double open,
        @SerializedName("close")
        Double close,
        @SerializedName("high")
        Double high,
        @SerializedName("low")
        Double low,
        @SerializedName("last")
        Double last,
        @SerializedName("volume")
        Double volume,
        @SerializedName("date")
        String date
) {
}
