package com.koleff.stockserver.stocks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.koleff.stockserver.stocks.domain.Stock;

public record StockExchangeDto(
        @JsonProperty("id")
        Long id,
        @JsonProperty("stock_id")
        Long stockId,
        @JsonProperty("name")
        String name,
        @JsonProperty("acronym")
        String acronym,
        @JsonProperty("exchange")
        String exchange,
        @JsonProperty("country")
        String country,
        @JsonProperty("country_code")
        String countryCode,
        @JsonProperty("city")
        String city,
        @JsonProperty("website")
        String website
//        @JsonProperty("stock")
//        @JsonIgnore
//        Stock stock
) {
}
