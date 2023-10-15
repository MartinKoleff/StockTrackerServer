package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockExchangeDtoMapper implements Function<StockExchange, StockExchangeDto> {
    @Override
    public StockExchangeDto apply(StockExchange stockExchange) {
        return new StockExchangeDto(
                stockExchange.getName(),
                stockExchange.getAcronym(),
                stockExchange.getExchange(),
                stockExchange.getCountry(),
                stockExchange.getCountryCode(),
                stockExchange.getCity(),
                stockExchange.getWebsite()
        );
    }
}
