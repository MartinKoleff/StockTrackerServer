package com.koleff.stockserver.stocks.domain.wrapper;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class DataWrapper<T> implements Serializable {

    @SerializedName("data")
    private List<T> data;
}
