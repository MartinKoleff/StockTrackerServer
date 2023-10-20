package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import com.koleff.stockserver.stocks.dto.mapper.StockExchangeDtoMapper;
import com.koleff.stockserver.stocks.exceptions.StockExchangeNotFoundException;
import com.koleff.stockserver.stocks.repository.StockExchangeRepository;
import com.koleff.stockserver.stocks.service.StockExchangeService;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockExchangeServiceImpl implements StockExchangeService {

    private final StockExchangeRepository stockExchangeRepository;
    private final StockExchangeDtoMapper stockExchangeDtoMapper;
    private final JsonUtil<DataWrapper<StockExchange>> jsonUtil;

    @Autowired
    public StockExchangeServiceImpl(StockExchangeRepository stockExchangeRepository,
                                    StockExchangeDtoMapper stockExchangeDtoMapper,
                                    JsonUtil<DataWrapper<StockExchange>> jsonUtil) {
        this.stockExchangeRepository = stockExchangeRepository;
        this.stockExchangeDtoMapper = stockExchangeDtoMapper;
        this.jsonUtil = jsonUtil;
    }

    /**
     * Get stock exchange from DB via id
     */
    @Override
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

    /**
     * Get stock exchanges filtered by country from DB
     */
    @Override
    public List<StockExchangeDto> getStockExchange(String country) {
        return stockExchangeRepository.getStockExchangeByCountry(country)
                .stream()
                .map(stockExchangeDtoMapper)
                .toList();
    }

    /**
     * Get all stock exchanges from DB
     */
    @Override
    public List<StockExchangeDto> getStockExchanges() {
        return stockExchangeRepository.findAll()
                .stream()
                .map(stockExchangeDtoMapper)
                .toList();
    }

    /**
     * Save one entry to DB
     */
    @Override
    public void saveStockExchange(StockExchange stockExchange) {
        stockExchangeRepository.save(stockExchange);
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveStockExchanges(List<StockExchange> stockExchanges) {
        stockExchangeRepository.saveAll(stockExchanges);
    }

    /**
     * Delete entry from DB via id
     */
    @Override
    public void deleteById(Long id) {
        stockExchangeRepository.deleteById(id);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        stockExchangeRepository.deleteAll();
    }

    /**
     * Load data from JSON
     */
    @Override
    public List<StockExchange> loadAllStockExchanges() {
        String json = jsonUtil.loadJson("exchangesV2.json");

        DataWrapper<StockExchange> data = jsonUtil.convertJson(json);
        System.out.println(data);

        return data.getData();
    }
}
