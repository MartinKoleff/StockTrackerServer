package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.dto.mapper.StockDtoMapper;
import com.koleff.stockserver.stocks.exceptions.StockNotFoundException;
import com.koleff.stockserver.stocks.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockDtoMapper stockDtoMapper;

    @Autowired
    public StockService(StockRepository stockRepository, StockDtoMapper stockDtoMapper){ //@Qualifier(value = "fake")
        this.stockRepository = stockRepository;
        this.stockDtoMapper = stockDtoMapper;
    }

    public List<StockDto> getStocks(){
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
                .orElseThrow(() -> new StockNotFoundException(
                        String.format("Stock with id %d not found.", id)));
    }
}
