package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import com.koleff.stockserver.stocks.dto.mapper.StockDtoMapper;
import com.koleff.stockserver.stocks.dto.mapper.StockExchangeDtoMapper;
import com.koleff.stockserver.stocks.exceptions.StockExchangeNotFoundException;
import com.koleff.stockserver.stocks.exceptions.StockNotFoundException;
import com.koleff.stockserver.stocks.repository.StockExchangeRepository;
import com.koleff.stockserver.stocks.repository.StockRepository;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockExchangeService {

    private final StockExchangeRepository stockExchangeRepository;
    private final StockExchangeDtoMapper stockExchangeDtoMapper;
    private final JsonUtil<DataWrapper<StockExchange>> jsonUtil;

    @Autowired
    public StockExchangeService(StockExchangeRepository stockExchangeRepository,
                        StockExchangeDtoMapper stockExchangeDtoMapper,
                        JsonUtil<DataWrapper<StockExchange>> jsonUtil) {
        this.stockExchangeRepository = stockExchangeRepository;
        this.stockExchangeDtoMapper = stockExchangeDtoMapper;
        this.jsonUtil = jsonUtil;
    }

    public List<StockExchangeDto> getStockExchanges() {
        return stockExchangeRepository.findAll()
                .stream()
                .map(stockExchangeDtoMapper)
                .toList();
    }

    public StockExchangeDto getStockExchange(Long id) {
        return stockExchangeRepository.findById(id)
                .stream()
                .map(stockExchangeDtoMapper)
                .findFirst()
                .orElseThrow(
                        () -> new StockExchangeNotFoundException(
                                String.format("Stock exchange with id %d not found.",
                                        id
                                )
                        )
                );
    }


    // TODO
    public StockExchangeDto getStockExchange(String country) {
        return null;
    }

    public void saveStockExchange(StockExchange stockExchange) {
        stockExchangeRepository.save(stockExchange);
    }

    public void saveStockExchanges(List<StockExchange> stockExchanges) {
        stockExchangeRepository.saveAll(stockExchanges);
    }

    // TODO
    //Load data from JSON
    public StockExchange loadStockExchange() {
        return null;
    }

    //Load data from JSON
    public List<StockExchange> loadStockExchanges() {
        jsonUtil.setType(StockExchange.class);
        String json = jsonUtil.loadJson("stockExchanges.json");

        DataWrapper<StockExchange> data = jsonUtil.convertJson(json);
        System.out.println(data);

        return data.getData();
    }
}
