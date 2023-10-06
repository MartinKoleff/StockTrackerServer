package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.StockDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StockDtoMapper implements Function<Stock, StockDto> {
    @Override
    public StockDto apply(Stock stock) {
        return new StockDto(
                stock.getId(),
                stock.getName(),
                stock.getTag(),
                stock.getCountry(),
                stock.getHasIntraDay(),
                stock.getHasEndOfDay()
        );

    }
}
