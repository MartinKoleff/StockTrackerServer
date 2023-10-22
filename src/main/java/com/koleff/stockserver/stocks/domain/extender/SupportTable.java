package com.koleff.stockserver.stocks.domain.extender;

/**
 * All duplicate functionality of these Entities:
 * - EndOfDay
 * - IntraDay
 * */
@Deprecated
public interface SupportTable {

    void setJoinById(Long id); //TODO: change to setJoinId and configure for all entities which join with other tables...
    void getJoinById(Long id); //TODO: change to setJoinId and configure for all entities which join with other tables...
}
