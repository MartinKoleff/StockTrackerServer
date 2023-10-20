package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.wrapper.StockExchangeExtended;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockExchangesExtendedMapper implements Function<StockExchangeExtended, StockExchange> {

    @Override
    public StockExchange apply(StockExchangeExtended stockExchangeExtended) {
        return new StockExchange(
                stockExchangeExtended.getId(),
                stockExchangeExtended.getName(),
                stockExchangeExtended.getAcronym(),
                stockExchangeExtended.getExchange(),
                stockExchangeExtended.getCountry(),
                stockExchangeExtended.getCountryCode(),
                stockExchangeExtended.getCity(),
                stockExchangeExtended.getWebsite(),
                stockExchangeExtended.getTimezoneId(),
                stockExchangeExtended.getCurrencyId()
        );
    }
}

