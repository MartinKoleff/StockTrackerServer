package com.koleff.stockserver.stocks.repository.custom;

public interface StockRepositoryCustom {
    void updateIntraDayStatus();
    void updateEndOfDayStatus();
}
