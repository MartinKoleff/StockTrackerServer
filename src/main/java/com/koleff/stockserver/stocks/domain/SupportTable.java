package com.koleff.stockserver.stocks.domain;

/**
 * All duplicate functionality of these Entities:
 * - EndOfDay
 * - IntraDay
 * - StockExchange
 * */
public interface SupportTable {

    void setStockId(Long id);
}
