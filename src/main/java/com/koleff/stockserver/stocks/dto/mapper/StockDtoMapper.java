package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.StockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockDtoMapper implements Function<Stock, StockDto> {

    private final IntraDayDtoMapper intraDayDtoMapper;

    private final EndOfDayDtoMapper endOfDayDtoMapper;
    private final StockExchangeDtoMapper stockExchangeDtoMapper;

    @Autowired
    public StockDtoMapper(IntraDayDtoMapper intraDayDtoMapper, EndOfDayDtoMapper endOfDayDtoMapper, StockExchangeDtoMapper stockExchangeDtoMapper) {
        this.intraDayDtoMapper = intraDayDtoMapper;
        this.endOfDayDtoMapper = endOfDayDtoMapper;
        this.stockExchangeDtoMapper = stockExchangeDtoMapper;
    }

    @Override
    public StockDto apply(Stock stock) {
        return new StockDto(
                stock.getName(),
                stock.getTag(),
                stock.getHasIntraDay(),
                stock.getHasEndOfDay(),
                stock.getEndOfDay()
                        .stream()
                        .map(endOfDayDtoMapper)
                        .toList(),
                stock.getIntraDay().stream()
                        .map(intraDayDtoMapper)
                        .toList(),
                stockExchangeDtoMapper.apply(stock.getStockExchange())
        );

    }
}
