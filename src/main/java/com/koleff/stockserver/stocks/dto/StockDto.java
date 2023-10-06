package com.koleff.stockserver.stocks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.koleff.stockserver.stocks.domain.Stock;

import java.util.ArrayList;
import java.util.List;

public record StockDto(
        Long id,
        String name,
        String tag,
        String country,
        Boolean hasIntraDay,
        Boolean hasEndOfDay
) {

    //ADD...
    //endOfDay
    //intraDay
    //stockExchange

//    public StockDto(Long id, String name, String tag, String country, Boolean hasIntraDay, Boolean hasEndOfDay) {
//        this.id = id;
//        this.name = name;
//        this.tag = tag;
//        this.country = country;
//        this.hasIntraDay = hasIntraDay;
//        this.hasEndOfDay = hasEndOfDay;
//    }
//
//    public StockDto(Stock stock){
//        this.id = stock.getId();
//        this.name = stock.getName();
//        this.tag = stock.getTag();
//        this.country = stock.getCountry();
//        this.hasIntraDay = stock.getHasIntraDay();
//        this.hasEndOfDay = stock.getHasEndOfDay();
//    }

    public String toJson() {
        return new Gson().toJson(this);
    }

//    public static StockDto fromStock(Stock stock) {
//        return new StockDto(stock);
//    }
//
//    public static List<StockDto> fromStocks(List<Stock> stocks) {
//        List<StockDto> dtoList = new ArrayList<>();
//        stocks.forEach(user -> dtoList.add(
//                StockDto.fromStock(user)
//        ));
//        return dtoList;
//    }
}
