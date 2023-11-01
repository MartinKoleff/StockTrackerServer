package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.IntraDayDto;

import java.util.List;

public interface IntraDayService {

    List<IntraDayDto> getIntraDays(String stockTag);
    List<IntraDayDto> getIntraDays(String stockTag, String dateFrom, String dateTo);
    IntraDayDto getIntraDay(String stockTag, String date);
    List<IntraDayDto> getIntraDays(Long id);
    List<List<IntraDayDto>> getAllIntraDays();
    void saveIntraDay(List<IntraDay> data);
    void saveAllIntraDays(List<List<IntraDay>> data);
    void saveViaJob();
    void deleteById(Long id);
    void deleteByStockTag(String stockTag);
    void deleteAll();
    List<IntraDay> loadIntraDays(String stockTag);
    List<List<IntraDay>> loadAllIntraDays();
}
