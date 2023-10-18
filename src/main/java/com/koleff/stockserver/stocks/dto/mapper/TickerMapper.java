package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.wrapper.Ticker;
import com.koleff.stockserver.stocks.domain.wrapper.StockWithStockExchange;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TickerMapper implements Function<StockWithStockExchange, Ticker> {

    @Override
    public Ticker apply(StockWithStockExchange stockWithStockExchange) {
        return new Ticker(
                stockWithStockExchange.getStockExchangeId(),
                stockWithStockExchange.getName(),
                stockWithStockExchange.getTag(),
                stockWithStockExchange.getHasIntraDay(),
                stockWithStockExchange.getHasEndOfDay()
        );

    }
}
