package com.koleff.stockserver.stocks.service;

import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.dto.mapper.StockDtoMapper;
import com.koleff.stockserver.stocks.exceptions.StockNotFoundException;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockDtoMapper stockDtoMapper;
    private final JsonUtil<DataWrapper> jsonUtil;

    @Autowired
    public StockService(StockRepository stockRepository,
                        StockDtoMapper stockDtoMapper,
                        JsonUtil<DataWrapper> jsonUtil) { //@Qualifier(value = "fake")
        this.stockRepository = stockRepository;
        this.stockDtoMapper = stockDtoMapper;
        this.jsonUtil = jsonUtil;
    }

    public List<StockDto> getStocks() {
        return stockRepository.findAll()
                .stream()
                .map(stockDtoMapper)
                .toList();
    }

    public StockDto getStock(Long id) {
        return stockRepository.findById(id)
                .stream()
                .map(stockDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new StockNotFoundException(
                                String.format("Stock with id %d not found.",
                                        id
                                )
                        )
                );
    }

    public void saveStock(String stockTag) {
    }

    public void saveStocks() {
        Type stockType = new TypeToken<DataWrapper<Stock>>() {
        }.getType();
        jsonUtil.setType(stockType);

        //Load data from json
        String json = jsonUtil.loadJson("tickers.json");
        DataWrapper<Stock> data = jsonUtil.convertJson(json);

        System.out.println(data);
        stockRepository.saveAll(data.getData());
    }


}
