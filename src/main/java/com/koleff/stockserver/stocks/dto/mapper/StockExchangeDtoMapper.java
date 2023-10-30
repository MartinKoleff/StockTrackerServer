package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockExchangeDtoMapper implements Function<StockExchange, StockExchangeDto> {

    private final TimezoneDtoMapper timezoneDtoMapper;
    private final CurrencyDtoMapper currencyDtoMapper;

    public StockExchangeDtoMapper(TimezoneDtoMapper timezoneDtoMapper, CurrencyDtoMapper currencyDtoMapper) {
        this.timezoneDtoMapper = timezoneDtoMapper;
        this.currencyDtoMapper = currencyDtoMapper;
    }

    @Override
    public StockExchangeDto apply(StockExchange stockExchange) {
        return new StockExchangeDto(
                stockExchange.getName(),
                stockExchange.getAcronym(),
                stockExchange.getCountry(),
                stockExchange.getCountryCode(),
                stockExchange.getCity(),
                stockExchange.getWebsite(),
                timezoneDtoMapper.apply(stockExchange.getTimezone()),
                currencyDtoMapper.apply(stockExchange.getCurrency())
        );
    }
}
