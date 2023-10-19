package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;

import java.util.List;

public interface EndOfDayService {

    List<EndOfDayDto> getEndOfDay(String stockTag);
    List<EndOfDayDto> getEndOfDay(Long id);
    List<List<EndOfDayDto>> getAllEndOfDays();
    void saveEndOfDay(List<EndOfDay> data);
    void saveAllEndOfDays(List<List<EndOfDay>> data);
    void deleteById(Long id);
    void deleteByStockTag(String stockTag);
    void deleteAll();
    List<EndOfDay> loadEndOfDay(String stockTag);
    List<List<EndOfDay>> loadAllEndOfDays();
}
