package com.koleff.stockserver.stocks.service;

import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.dto.mapper.IntraDayDtoMapper;
import com.koleff.stockserver.stocks.exceptions.IntraDayNotFoundException;
import com.koleff.stockserver.stocks.exceptions.IntraDayNotSavedException;
import com.koleff.stockserver.stocks.repository.IntraDayRepository;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class IntraDayService {
    private final IntraDayRepository intraDayRepository;
    private final StockRepository stockRepository;
    private final IntraDayDtoMapper intraDayDtoMapper;
    private final JsonUtil<DataWrapper> jsonUtil;

    @Autowired
    public IntraDayService(IntraDayRepository intraDayRepository,
                           IntraDayDtoMapper intraDayDtoMapper,
                           StockRepository stockRepository, 
                           JsonUtil<DataWrapper> jsonUtil) {
        this.intraDayRepository = intraDayRepository;
        this.intraDayDtoMapper = intraDayDtoMapper;
        this.stockRepository = stockRepository;
        this.jsonUtil = jsonUtil;
    }

    public List<IntraDayDto> getIntraDayList() {
        return intraDayRepository.findAll()
                .stream()
                .map(intraDayDtoMapper)
                .toList();
    }

    public IntraDayDto getIntraDay(Long id) {
        return intraDayRepository.findById(id)
                .stream()
                .map(intraDayDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new IntraDayNotFoundException(
                                String.format("Intra day with id %d not found.", id
                                )
                        )
                );
    }

    public void saveIntraDay() {
        Type intraDayType = new TypeToken<DataWrapper<IntraDay>>() {}.getType();
        jsonUtil.setType(intraDayType);

        stockRepository.findAll().stream()
                .map(Stock::getTag)
                .forEach(stockTag -> {
                    //Configure json based on current stock
                    String filePath = String.format("intraday%s.json", stockTag);

                    //Load data from json
                    String json = jsonUtil.loadJson(filePath);
                    DataWrapper<IntraDay> data = jsonUtil.convertJson(json);

                    //Configure stock_id
                    data.getData()
                            .forEach(intraDay -> {
                                intraDay.setStockId(
                                        stockRepository.findStockByStockTag(stockTag)
                                                .orElseThrow(
                                                        () -> new IntraDayNotSavedException(
                                                                String.format(
                                                                        "Intra day for stock %s could not be saved.", stockTag
                                                                )
                                                        )
                                                )
                                                .getId()
                                );
                            });
                    //Save data entities to DB
                    intraDayRepository.saveAll(data.getData());
                });
    }
}
