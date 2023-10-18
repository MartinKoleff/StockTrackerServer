package com.koleff.stockserver.stocks.utils.tickersUtil;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.Ticker;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.domain.wrapper.StockWithStockExchange;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import com.koleff.stockserver.stocks.dto.mapper.TickerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TickersUtil {

    private final JsonUtil<DataWrapper<StockExchange>> stockExchangeJsonUtil;
    private final JsonUtil<DataWrapper<StockWithStockExchange>> stockWithExchangeJsonUtil;
    private final JsonUtil<DataWrapper<Ticker>> tickerJsonUtil;
    private final TickerMapper tickerMapper;

    @Autowired
    public TickersUtil(JsonUtil<DataWrapper<StockExchange>> stockExchangeJsonUtil,
                       JsonUtil<DataWrapper<StockWithStockExchange>> stockWithExchangeJsonUtil,
                       JsonUtil<DataWrapper<Ticker>> tickerJsonUtil,
                       TickerMapper tickerMapper) {
        this.stockExchangeJsonUtil = stockExchangeJsonUtil;
        this.stockWithExchangeJsonUtil = stockWithExchangeJsonUtil;
        this.tickerJsonUtil = tickerJsonUtil;
        this.tickerMapper = tickerMapper;
    }

    public void configureStockExchangeId() {
        //Load tickers JSON
        String tickersJson = stockWithExchangeJsonUtil.loadJson("tickers.json");
        DataWrapper<StockWithStockExchange> tickers = stockWithExchangeJsonUtil.convertJson(tickersJson);

        //Load stock exchanges JSON
        String stockExchangesJson = stockExchangeJsonUtil.loadJson("stockExchanges.json");
        List<StockExchange> exchanges = stockExchangeJsonUtil.convertJson(stockExchangesJson).getData();


        //Replace stockExchange string with stock_exchange_id based on entry from stockExchange list...
        tickers.getData()
                .forEach(stock -> stock.setStockExchangeId(
                                (long) (exchanges.indexOf(
                                        exchanges.stream()
                                                .filter(exchange ->
                                                        exchange.getName().equals(stock.getStockExchange().getName())
                                                )
                                                .findFirst()
                                                .orElseThrow() //TODO: add error...
                                ) + 1)
                        )
                );

        //Convert StockWithExchange to Stock
        DataWrapper<Ticker> tickersWithExchangeId = new DataWrapper<Ticker>();

        tickersWithExchangeId.setData(
                tickers.getData()
                        .stream()
                        .map(tickerMapper)
                        .toList()
        );

        //Convert to JSON
        System.out.println(tickersWithExchangeId.getData());
        tickerJsonUtil.exportToJson(tickersWithExchangeId, "tickersV2");
    }

    //TODO: NEW CLASS -> Create one for StockExchange to configure timezone_id and currency_id
}
