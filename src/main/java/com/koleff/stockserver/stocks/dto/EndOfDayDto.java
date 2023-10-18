package com.koleff.stockserver.stocks.dto;

import com.google.gson.annotations.SerializedName;

public record EndOfDayDto(
        @SerializedName("open")
        Double open,
        @SerializedName("close")
        Double close,
        @SerializedName("high")
        Double high,
        @SerializedName("low")
        Double low,
        @SerializedName("volume")
        Double volume,
        @SerializedName("adj_open")
        Double adjustOpen,
        @SerializedName("adj_close")
        Double adjustClose,
        @SerializedName("adj_high")
        Double adjustHigh,
        @SerializedName("adj_low")
        Double adjustLow,
        @SerializedName("adj_volume")
        Double adjustVolume,
        @SerializedName("split_factor")
        Double splitFactor,
        @SerializedName("date")
        String date
) {
}
