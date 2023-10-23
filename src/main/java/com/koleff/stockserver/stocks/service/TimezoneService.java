package com.koleff.stockserver.stocks.service;


import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.dto.TimezoneDto;

import java.util.List;

public interface TimezoneService {

    TimezoneDto getTimezone(Long id);
    TimezoneDto getTimezone(String stockTag);
    Long getTimezoneId(String timezone);
    List<TimezoneDto> getTimezones();
    List<String> getTimezoneStrings(); //TODO: rename
    void saveTimezone(Timezone timezone);
    void saveTimezones(List<Timezone> data);
    void deleteById(Long id);
    void deleteByTimezone(String timezone);
    void deleteAll();
    Timezone loadTimezone(String timezone);
    List<Timezone> loadAllTimezones();
}