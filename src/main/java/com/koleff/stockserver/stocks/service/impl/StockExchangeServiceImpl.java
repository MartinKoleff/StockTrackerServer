package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import com.koleff.stockserver.stocks.dto.mapper.StockExchangeDtoMapper;
import com.koleff.stockserver.stocks.exceptions.StockExchangeNotFoundException;
import com.koleff.stockserver.stocks.repository.impl.StockExchangeRepositoryImpl;
import com.koleff.stockserver.stocks.service.StockExchangeService;
import com.koleff.stockserver.stocks.utils.jsonUtil.StockExchangeJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockExchangeServiceImpl implements StockExchangeService {

    @Value("${koleff.versionAnnotation}") //Configuring version annotation for Json loading / exporting
    private String versionAnnotation;
    private final StockExchangeRepositoryImpl stockExchangeRepositoryImpl;
    private final StockExchangeDtoMapper stockExchangeDtoMapper;
    private final StockExchangeJsonUtil stockExchangeJsonUtil;

    @Autowired
    public StockExchangeServiceImpl(StockExchangeRepositoryImpl stockExchangeRepositoryImpl,
                                    StockExchangeDtoMapper stockExchangeDtoMapper,
                                    StockExchangeJsonUtil stockExchangeJsonUtil) {
        this.stockExchangeRepositoryImpl = stockExchangeRepositoryImpl;
        this.stockExchangeDtoMapper = stockExchangeDtoMapper;
        this.stockExchangeJsonUtil = stockExchangeJsonUtil;
    }

    /**
     * Get stock exchange from DB via id
     */
    @Override
    public StockExchangeDto getStockExchange(Long id) {
        return stockExchangeRepositoryImpl.findById(id)
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
        return stockExchangeRepositoryImpl.findByCountry(country)
                .stream()
                .map(stockExchangeDtoMapper)
                .toList();
    }

    /**
     * Get all stock exchanges from DB
     */
    @Override
    public List<StockExchangeDto> getStockExchanges() {
        return stockExchangeRepositoryImpl.findAll()
                .stream()
                .map(stockExchangeDtoMapper)
                .toList();
    }

    /**
     * Save one entry to DB
     */
    @Override
    public void saveStockExchange(StockExchange stockExchange) {
        stockExchangeRepositoryImpl.save(stockExchange);
    }

    /**
     * Save bulk entries to DB
     */
    @Override
    public void saveStockExchanges(List<StockExchange> stockExchanges) {
        stockExchangeRepositoryImpl.saveAll(stockExchanges);
    }

    /**
     * Delete entry from DB via id
     */
    @Override
    public void deleteById(Long id) {
        stockExchangeRepositoryImpl.deleteById(id);
    }

    /**
     * Delete all entries in DB
     */
    @Override
    public void deleteAll() {
        stockExchangeRepositoryImpl.deleteAll();
    }

    /**
     * Load data from JSON
     */
    @Override
    public List<StockExchange> loadAllStockExchanges() {
        //Configure json based on current stock
        String filePath = String.format("exchanges%s.json", versionAnnotation);

        //Load data from json
        String json = stockExchangeJsonUtil.loadJson(filePath);

        DataWrapper<StockExchange> data = stockExchangeJsonUtil.convertJson(json);

        return data.getData();
    }
}
