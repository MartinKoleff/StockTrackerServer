package com.koleff.stockserver.stocks.utils.tickersUtil;

import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.wrapper.Ticker;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.domain.wrapper.StockWithStockExchange;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import com.koleff.stockserver.stocks.dto.mapper.TickerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TickersUtil {
    @Value("${koleff.versionAnnotation}")
    private String versionAnnotation;
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

    public void configureIds(){
        DataWrapper<Ticker> tickersWithExchangeId = new DataWrapper<>();

        configureStockExchangeId(tickersWithExchangeId);

        exportToJson(tickersWithExchangeId);
    }

    /**
     * Using V1 JSON and configuring its ids and exporting the result to V2
     */
    private void configureStockExchangeId(DataWrapper<Ticker> tickersWithExchangeId) {
        //Load tickers JSON
        String tickersJson = stockWithExchangeJsonUtil.loadJson("tickers.json");
        DataWrapper<StockWithStockExchange> tickers = stockWithExchangeJsonUtil.convertJson(tickersJson);

        //Load stock exchanges JSON
        String stockExchangesJson = stockExchangeJsonUtil.loadJson("exchangesV2.json");
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
        tickersWithExchangeId.setData(
                tickers.getData()
                        .stream()
                        .map(tickerMapper)
                        .toList()
        );
    }

    private void exportToJson(DataWrapper<Ticker> tickersWithExchangeId){

        //Convert to JSON
        tickerJsonUtil.exportToJson(tickersWithExchangeId, "tickers", versionAnnotation); //TODO: app.properties
    }
}
