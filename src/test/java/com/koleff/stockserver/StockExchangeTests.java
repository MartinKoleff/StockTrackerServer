package com.koleff.stockserver;

import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.service.impl.CurrencyServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockExchangeServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.service.impl.TimezoneServiceImpl;
import com.koleff.stockserver.stocks.utils.stockExchangesUtil.StockExchangesUtil;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Using VM options to configure PostgreSQL DB login
 */
@SpringBootTest(
        webEnvironment = RANDOM_PORT
)
@TestInstance(PER_CLASS)
@ContextConfiguration(
        classes = {StockServerApplication.class, TestConfiguration.class}
)
@ExtendWith(SpringExtension.class)
public class StockExchangeTests {
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private final StockServiceImpl stockServiceImpl;

    @Qualifier("logger")
    private final Logger logger;

    private final StockExchangesUtil stockExchangesUtil;

    @Autowired
    StockExchangeTests(StockExchangeServiceImpl stockExchangeServiceImpl,
                       CurrencyServiceImpl currencyServiceImpl,
                       TimezoneServiceImpl timezoneServiceImpl,
                       StockServiceImpl stockServiceImpl,
                       StockExchangesUtil stockExchangesUtil,
                       Logger logger) {
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.currencyServiceImpl = currencyServiceImpl;
        this.timezoneServiceImpl = timezoneServiceImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.stockExchangesUtil = stockExchangesUtil;
        this.logger = logger;
    }

    @Test
    @Order(2)
    void stockExchangesLoadingTest() {
        List<Currency> currencies = currencyServiceImpl.loadAllCurrencies();
        List<Timezone> timezones = timezoneServiceImpl.loadAllTimezones();
        List<Stock> stocks = stockServiceImpl.loadAllStocks();
        List<StockExchange> stockExchanges = stockExchangeServiceImpl.loadAllStockExchanges();

        currencyServiceImpl.saveCurrencies(currencies);
        timezoneServiceImpl.saveTimezones(timezones);
        stockExchangeServiceImpl.saveStockExchanges(stockExchanges);
        stockServiceImpl.saveStocks(stocks);

        Assertions.assertNotNull(stockExchangeServiceImpl.getStockExchanges());
    }

    @Test
    @Order(1)
    void stockExchangesConfigureIdsTest() {
        stockExchangesUtil.configureIds();

        List<StockExchange> exchanges = stockExchangeServiceImpl.loadAllStockExchanges();

        boolean currencyIdsUpdated = exchanges.stream()
                .map(StockExchange::getCurrencyId)
                .anyMatch(Objects::isNull);

        boolean timezoneIdsUpdated = exchanges.stream()
                .map(StockExchange::getTimezoneId)
                .anyMatch(Objects::isNull);

        Assertions.assertFalse(currencyIdsUpdated && timezoneIdsUpdated);
    }
}
