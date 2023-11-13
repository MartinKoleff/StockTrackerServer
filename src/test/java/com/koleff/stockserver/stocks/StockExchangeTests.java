package com.koleff.stockserver.stocks;

import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.CurrencyDto;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import com.koleff.stockserver.stocks.dto.TimezoneDto;
import com.koleff.stockserver.stocks.resources.DatabaseSetupExtension;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.stocks.service.impl.*;
import com.koleff.stockserver.stocks.utils.stockExchangesUtil.StockExchangesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Using VM options to configure PostgreSQL DB login
 */
@SpringBootTest(
        webEnvironment = RANDOM_PORT
)
@ContextConfiguration(
        classes = {TestConfiguration.class}
)
@ExtendWith(DatabaseSetupExtension.class)
public class StockExchangeTests {

    private final static Logger logger = LogManager.getLogger(StockExchangeTests.class);
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangesUtil stockExchangesUtil;
    private boolean isDoneTesting = false;
    private boolean hasInitializedDB = false;
    private long startTime;
    private long endTime;
    private long totalTime;

    @Autowired
    StockExchangeTests(StockExchangeServiceImpl stockExchangeServiceImpl,
                       CurrencyServiceImpl currencyServiceImpl,
                       TimezoneServiceImpl timezoneServiceImpl,
                       StockServiceImpl stockServiceImpl,
                       StockExchangesUtil stockExchangesUtil) {
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.currencyServiceImpl = currencyServiceImpl;
        this.timezoneServiceImpl = timezoneServiceImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.stockExchangesUtil = stockExchangesUtil;
    }

    @BeforeEach
    public void setup() {
        logger.info("Setup before test starts...");

        //Load and Save stocks to DB
        List<Stock> stocks = stockServiceImpl.loadAllStocks();


        //Need to load and save stock_exchange before saving stock entity
        List<Currency> currencies = currencyServiceImpl.loadAllCurrencies();
        List<Timezone> timezones = timezoneServiceImpl.loadAllTimezones();
        List<StockExchange> stockExchanges = stockExchangeServiceImpl.loadAllStockExchanges();

        currencyServiceImpl.saveCurrencies(currencies);
        timezoneServiceImpl.saveTimezones(timezones);
        stockExchangeServiceImpl.saveStockExchanges(stockExchanges);

        stockServiceImpl.saveStocks(stocks);

        startTime = System.currentTimeMillis();

        hasInitializedDB = true;
    }

    @AfterEach
    public void tearDown() {
        endTime = System.currentTimeMillis() - startTime;
        totalTime = endTime - startTime;
        logger.info(String.format("Starting time: %d\n Finish time: %d\n Total time: %d", startTime, endTime, totalTime));

        logger.info("Setup after test ends...");
        logger.info("Deleting all DB entries...");

        boolean isDBEmpty = stockServiceImpl.getStocks().isEmpty()
                && stockExchangeServiceImpl.getStockExchanges().isEmpty()
                && currencyServiceImpl.getCurrencies().isEmpty()
                && timezoneServiceImpl.getTimezones().isEmpty();
        logger.info(String.format("DB is empty: %s", isDBEmpty));
    }

    @Nested
    class StockExchangeLoadingNestedClass {
        @Test
        @Order(1)
        @DisplayName("Fetching data from DB.")
        void stockExchangesLoadingTest() {
            List<StockExchangeDto> exchanges = stockExchangeServiceImpl.getStockExchanges();
            logger.debug(exchanges);

            assertAll(
                    "Validation of exchanges fetching data from DB.",
                    () -> assertNotNull(exchanges),
                    () -> assertFalse(exchanges.isEmpty())
            );
        }

        @Test
        @Order(2)
        @DisplayName("Fetching data from DB. Checking relation between StockExchange and Timezone entity")
        void stockExchangesLoadingTimezoneTest() {
            List<StockExchangeDto> exchanges = stockExchangeServiceImpl.getStockExchanges();
            logger.debug(exchanges);

            List<TimezoneDto> timezones = exchanges.stream()
                    .map(StockExchangeDto::timezoneDto)
                    .toList();

            assertAll(
                    "Validation of timezone fetching data from DB.",
                    () -> assertNotNull(timezones),
                    () -> assertFalse(timezones.isEmpty())
            );
        }

        @Test
        @Order(3)
        @DisplayName("Fetching data from DB. Checking relation between StockExchange and Currency entity")
        void stockExchangesLoadingCurrencyTest() {
            List<StockExchangeDto> exchanges = stockExchangeServiceImpl.getStockExchanges();
            logger.debug(exchanges);

            List<CurrencyDto> currencies = exchanges.stream()
                    .map(StockExchangeDto::currencyDto)
                    .toList();

            assertAll(
                    "Validation of currency fetching data from DB.",
                    () -> assertNotNull(currencies),
                    () -> assertFalse(currencies.isEmpty())
            );
        }
    }

    @Test
    @Order(2)
    @Disabled("Exports to JSON functionality will change current V2 JSON files...")
    //TODO: add test profile to determine if export or no / delete files / use new naming for testing
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

        assertFalse(currencyIdsUpdated && timezoneIdsUpdated);

        isDoneTesting = true;
    }
}
