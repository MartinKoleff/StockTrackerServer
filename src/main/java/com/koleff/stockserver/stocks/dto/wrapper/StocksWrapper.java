package com.koleff.stockserver.stocks.dto.wrapper;

import com.google.gson.annotations.SerializedName;
import com.koleff.stockserver.stocks.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class StocksWrapper implements Serializable {

    @SerializedName("data")
    private List<Stock> stockList;
}
