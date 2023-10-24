package com.koleff.stockserver;

import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import com.koleff.stockserver.stocks.service.impl.*;
import com.koleff.stockserver.stocks.utils.stockExchangesUtil.StockExchangesUtil;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
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
    private boolean isDoneTesting = false;
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

    @BeforeEach
    public void setup() {
        logger.info("Setup before test starts...");
        logger.info("Deleting all DB entries...");

        stockExchangeServiceImpl.deleteAll();
        currencyServiceImpl.deleteAll();
        timezoneServiceImpl.deleteAll();

        boolean isDBEmpty = stockExchangeServiceImpl.getStockExchanges().isEmpty()
                && currencyServiceImpl.getCurrencies().isEmpty()
                && timezoneServiceImpl.getTimezones().isEmpty();
        logger.info("DB is empty: %s", isDBEmpty);
    }

    @AfterEach
    public void tearDown() {
        if (isDoneTesting){
            logger.info("Testing finished!");
            return;
        }

        logger.info("Deleting all DB entries...");
        stockExchangeServiceImpl.deleteAll();
        currencyServiceImpl.deleteAll();
        timezoneServiceImpl.deleteAll();

        boolean isDBEmpty = stockExchangeServiceImpl.getStockExchanges().isEmpty()
                            && currencyServiceImpl.getCurrencies().isEmpty()
                            && timezoneServiceImpl.getTimezones().isEmpty();
        logger.info("DB is empty: %s", isDBEmpty);
    }

    @Test
    @Order(1)
    @DisplayName("Loading from DB test. Relation between entities test.")
    void stockExchangesLoadingTest() {
        List<Currency> currencies = currencyServiceImpl.loadAllCurrencies();
        List<Timezone> timezones = timezoneServiceImpl.loadAllTimezones();
        List<StockExchange> stockExchanges = stockExchangeServiceImpl.loadAllStockExchanges();
        List<Stock> stocks = stockServiceImpl.loadAllStocks();

        //Saving order matters!
        currencyServiceImpl.saveCurrencies(currencies);
        timezoneServiceImpl.saveTimezones(timezones);
        stockExchangeServiceImpl.saveStockExchanges(stockExchanges);
        stockServiceImpl.saveStocks(stocks);

        List<StockExchangeDto> exchanges = stockExchangeServiceImpl.getStockExchanges();
        logger.debug(exchanges);

        Assertions.assertNotNull(exchanges);
    }

    @Test
    @Order(2)
    @DisplayName("Configuring currency_id and timezone_id from tickers.json and exporting to tickersV2.json test.")
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

        isDoneTesting = true;
    }
}
