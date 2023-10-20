package com.koleff.stockserver.stocks.utils.stockExchangesUtil;

import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.domain.wrapper.StockExchangeExtended;
import com.koleff.stockserver.stocks.dto.mapper.StockExchangesExtendedMapper;
import com.koleff.stockserver.stocks.utils.jsonUtil.base.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockExchangesUtil {
    private final JsonUtil<DataWrapper<StockExchange>> stockExchangeJsonUtil;
    private final JsonUtil<DataWrapper<StockExchangeExtended>> stockExchangeExtendedJsonUtil;
    private final JsonUtil<DataWrapper<Currency>> currencyJsonUtil;
    private final JsonUtil<DataWrapper<Timezone>> timezoneJsonUtil;
    private final StockExchangesExtendedMapper stockExchangesExtendedMapper;

    public StockExchangesUtil(JsonUtil<DataWrapper<StockExchange>> stockExchangeJsonUtil,
                              JsonUtil<DataWrapper<StockExchangeExtended>> stockExchangeExtendedJsonUtil,
                              JsonUtil<DataWrapper<Currency>> currencyJsonUtil,
                              JsonUtil<DataWrapper<Timezone>> timezoneJsonUtil,
                              StockExchangesExtendedMapper stockExchangesExtendedMapper) {
        this.stockExchangeJsonUtil = stockExchangeJsonUtil;
        this.stockExchangeExtendedJsonUtil = stockExchangeExtendedJsonUtil;
        this.currencyJsonUtil = currencyJsonUtil;
        this.timezoneJsonUtil = timezoneJsonUtil;
        this.stockExchangesExtendedMapper = stockExchangesExtendedMapper;
    }

    public void configureIds() {
        DataWrapper<StockExchange> exchanges = new DataWrapper<>();

        DataWrapper<StockExchangeExtended> exchangesExtended = loadData();

        configureCurrencyId(exchangesExtended);
        configureTimezoneId(exchangesExtended);

        updateData(exchanges, exchangesExtended);
        exportToJson(exchanges);
    }

    private DataWrapper<StockExchangeExtended> loadData() {
        //Load stockExchanges JSON
        String stockExchangeExtendedJson = stockExchangeExtendedJsonUtil.loadJson("exchanges.json");
        DataWrapper<StockExchangeExtended> stockExchangeExtended = stockExchangeExtendedJsonUtil.convertJson(stockExchangeExtendedJson);

        return stockExchangeExtended;
    }

    private void updateData(DataWrapper<StockExchange> exchanges, DataWrapper<StockExchangeExtended> exchangesExtended) {
        exchanges.setData(
                exchangesExtended.getData()
                        .stream()
                        .map(stockExchangesExtendedMapper)
                        .toList()
        );
    }

    private void configureCurrencyId(DataWrapper<StockExchangeExtended> stockExchangeExtended) { //TODO: return stockExchange to do method chaining
        //Load currencies JSON
        String currenciesJson = currencyJsonUtil.loadJson("currencies.json");
        List<Currency> currencies = currencyJsonUtil.convertJson(currenciesJson).getData();

        //Replace currency with currency_id based on entry from currency list...
        stockExchangeExtended.getData()
                .forEach(exchange -> exchange.setCurrencyId(
                                (long) (currencies.indexOf(
                                        currencies.stream()
                                                .filter(currency ->
                                                        currency.getName().equals(
                                                                exchange.getCurrency().getName()
                                                        )
                                                )
                                                .findFirst()
                                                .orElseThrow() //TODO: add error...
                                ) + 1)
                        )
                );
    }

    private void configureTimezoneId(DataWrapper<StockExchangeExtended> stockExchangeExtended) {
        //Load currencies JSON
        String timezonesJson = timezoneJsonUtil.loadJson("timezones.json");
        List<Timezone> timezones = timezoneJsonUtil.convertJson(timezonesJson).getData();

        //Replace timezone with timezone_id based on entry from timezone list...
        stockExchangeExtended.getData()
                .forEach(exchange -> exchange.setTimezoneId(
                                (long) (timezones.indexOf(
                                        timezones.stream()
                                                .filter(timezone ->
                                                        timezone.getAbbreviation().equals(
                                                                exchange.getTimezone().getAbbreviation()
                                                        )
                                                )
                                                .findFirst()
                                                .orElseThrow() //TODO: add error...
                                ) + 1)
                        )
                );
    }

    private void exportToJson(DataWrapper<StockExchange> exchanges) {
        //Convert to JSON
        System.out.println(exchanges.getData());
        stockExchangeJsonUtil.exportToJson(exchanges, "exchangesV2");
    }
}
