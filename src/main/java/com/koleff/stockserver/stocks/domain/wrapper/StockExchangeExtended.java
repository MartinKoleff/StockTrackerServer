package com.koleff.stockserver.stocks.domain.wrapper;

import com.google.gson.annotations.SerializedName;
import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.Timezone;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class StockExchangeExtended implements Serializable {
 
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("acronym")
    private String acronym;

    @SerializedName("exchange")
    private String exchange;

    @SerializedName("country")
    private String country;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("city")
    private String city;

    @SerializedName("website")
    private String website;

    @SerializedName("timezone_id")
    private Long timezoneId;
    
    @SerializedName("timezone")
    private Timezone timezone;

    @SerializedName("currency_id")
    private Long currencyId;

    @SerializedName("currency")
    private Currency currency;

    @Override
    public String toString() {
        return "StockExchange{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", exchange='" + exchange + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", city='" + city + '\'' +
                ", website='" + website + '\'' +
                ", timezone=" + timezone +
                ", currency=" + currency +
                '}';
    }
}
