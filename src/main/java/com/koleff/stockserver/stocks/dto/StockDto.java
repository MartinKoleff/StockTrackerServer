package com.koleff.stockserver.stocks.dto;

import com.google.gson.annotations.SerializedName;
import jakarta.annotation.Nullable;

import java.util.List;

public record StockDto (
        @SerializedName("id")
        Long id,

        @SerializedName("name")
        String name,

        @SerializedName("tag")
         String tag,

        @SerializedName("has_intraday")
        Boolean hasIntraDay,

        @SerializedName("has_end_of_day")
        Boolean hasEndOfDay,

        @SerializedName("end_of_day")
        List<EndOfDayDto> endOfDayDtosList,

        @SerializedName("intra_day")
        List<IntraDayDto> intraDayDtosList,

        @SerializedName("stock_exchange")
        StockExchangeDto stockExchangeDto
){
        
}
