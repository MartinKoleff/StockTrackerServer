package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;

import java.util.List;

public interface EndOfDayService {

    List<EndOfDayDto> getEndOfDays(String stockTag);
    List<EndOfDayDto> getEndOfDays(String stockTag, String dateFrom, String dateTo);
    EndOfDayDto getEndOfDay(String stockTag, String date);
    List<EndOfDayDto> getEndOfDays(Long id);
    List<List<EndOfDayDto>> getAllEndOfDays();
    void saveEndOfDay(List<EndOfDay> data);
    void saveAllEndOfDays(List<List<EndOfDay>> data);
    void saveViaJob();
    void deleteById(Long id);
    void deleteByStockTag(String stockTag);
    void deleteAll();
    void truncateTable();
    List<EndOfDay> loadEndOfDays(String stockTag);
    List<List<EndOfDay>> loadAllEndOfDays();
}
