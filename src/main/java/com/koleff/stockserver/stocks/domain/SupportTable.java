package com.koleff.stockserver.stocks.domain;

/**
 * All duplicate functionality of these Entities:
 * - EndOfDay
 * - IntraDay
 * */
public interface SupportTable {

    void setStockId(Long id); //TODO: change to setJoinId and configure for all entities which join with other tables...
}
