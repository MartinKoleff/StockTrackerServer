package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.IntraDayDto;

import java.util.List;

public interface IntraDayService {

    List<IntraDayDto> getIntraDay(String stockTag);
    List<IntraDayDto> getIntraDay(Long id);
    List<List<IntraDayDto>> getAllIntraDays();
    void saveIntraDay(List<IntraDay> data);
    void saveAllIntraDays(List<List<IntraDay>> data);
    void saveViaJob();
    void deleteById(Long id);
    void deleteByStockTag(String stockTag);
    void deleteAll();
    List<IntraDay> loadIntraDay(String stockTag);
    List<List<IntraDay>> loadAllIntraDays();
}
