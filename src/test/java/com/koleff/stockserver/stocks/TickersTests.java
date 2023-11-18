package com.koleff.stockserver.stocks;

import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.dto.StockExchangeDto;
import com.koleff.stockserver.stocks.resources.DatabaseSetupExtension;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.stocks.service.impl.*;
import com.koleff.stockserver.stocks.utils.tickersUtil.TickersUtil;
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
public class TickersTests {
    private final static Logger logger = LogManager.getLogger(TickersTests.class);
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private final EndOfDayServiceImpl endOfDayServiceImpl;
    private final IntraDayServiceImpl intraDayServiceImpl;
    private final TickersUtil tickersUtil;
    private boolean isDoneTesting = false;
    private boolean hasInitializedDB = false;
    private long startTime;
    private long endTime;
    private long totalTime;

    @Autowired
    TickersTests(StockServiceImpl stockServiceImpl,
                 StockExchangeServiceImpl stockExchangeServiceImpl,
                 CurrencyServiceImpl currencyServiceImpl,
                 TimezoneServiceImpl timezoneServiceImpl,
                 EndOfDayServiceImpl endOfDayServiceImpl,
                 IntraDayServiceImpl intraDayServiceImpl,
                 TickersUtil tickersUtil) {
        this.stockServiceImpl = stockServiceImpl;
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.currencyServiceImpl = currencyServiceImpl;
        this.timezoneServiceImpl = timezoneServiceImpl;
        this.endOfDayServiceImpl = endOfDayServiceImpl;
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.tickersUtil = tickersUtil;
    }

    @BeforeEach
    public void setup() {
        logger.info("Setup before test starts...");

        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

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

        intraDayServiceImpl.saveAllIntraDays(intraDays);

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
                && intraDayServiceImpl.getAllIntraDays().isEmpty()
                && endOfDayServiceImpl.getAllEndOfDays().isEmpty()
                && stockExchangeServiceImpl.getStockExchanges().isEmpty()
                && currencyServiceImpl.getCurrencies().isEmpty()
                && timezoneServiceImpl.getTimezones().isEmpty();

        logger.info(String.format("DB is empty: %s", isDBEmpty));
    }

    @Nested
    class TickersLoadingNestedClass {
        @Test
        @Order(1)
        @DisplayName("Fetching data from DB.")
        void tickersLoadingTest() {
            List<StockDto> stockDtos = stockServiceImpl.getStocks();
            logger.debug(stockDtos);

            assertAll(
                    "Validation of stock fetching data from DB.",
                    () -> assertNotNull(stockDtos),
                    () -> assertFalse(stockDtos.isEmpty())
            );
        }

        @Test
        @Order(2)
        @DisplayName("Fetching data from DB. Checking relation between Stock and EOD entity")
        void tickersLoadingEODTest() {
            List<StockDto> stockDtos = stockServiceImpl.getStocks();
            logger.debug(stockDtos);

            List<List<EndOfDayDto>> eodDtos = stockDtos.stream()
                    .map(StockDto::endOfDayDtosList)
                    .toList();

            assertAll(
                    "Validation of end of days fetching data from DB.",
                    () -> assertNotNull(eodDtos),
                    () -> assertFalse(eodDtos.isEmpty())
            );
        }

        @Test
        @Order(3)
        @DisplayName("Fetching data from DB. Checking relation between Stock and IntraDay entity")
        void tickersLoadingIntraDayTest() {
            List<StockDto> stockDtos = stockServiceImpl.getStocks();
            logger.debug(stockDtos);

            List<List<IntraDayDto>> intraDayDtos = stockDtos.stream()
                    .map(StockDto::intraDayDtosList)
                    .toList();

            assertAll(
                    "Validation of intra day fetching data from DB.",
                    () -> assertNotNull(intraDayDtos),
                    () -> assertFalse(intraDayDtos.isEmpty())
            );
        }

        @Test
        @Order(4)
        @DisplayName("Fetching data from DB. Checking relation between Stock and StockExchange entity")
        void tickersLoadingStockExchangeTest() {
            List<StockDto> stockDtos = stockServiceImpl.getStocks();
            logger.debug(stockDtos);

            List<StockExchangeDto> stockExchangeDtos = stockDtos.stream()
                    .map(StockDto::stockExchangeDto)
                    .toList();

            assertAll(
                    "Validation of exchanges fetching data from DB.",
                    () -> assertNotNull(stockExchangeDtos),
                    () -> assertFalse(stockExchangeDtos.isEmpty())
            );
        }
    }

    @Test
    @Order(2)
    @DisplayName("Configuring stock_exchange_id from exchanges.json and exporting to exchangesV2.json test.")
    @Disabled("Exports to JSON functionality will change current V2 JSON files...")//TODO: add test profile to determine if export or no / delete files / use new naming for testing
    void tickersConfigureStockExchangeIdTest() {
        tickersUtil.configureIds();

        List<Stock> stocks = stockServiceImpl.loadAllStocks();

        Assertions.assertFalse(stocks.stream()
                .map(Stock::getStockExchangeId)
                .anyMatch(Objects::isNull)
        );
    }

    @Test
    @Order(3)
    @DisplayName("Update hasIntraDay test.")
    void tickersUpdateHasIntraDayTest() {
        //Clear DB
        stockServiceImpl.deleteAll();
        intraDayServiceImpl.deleteAll();

        //Load stock
        Stock stock = stockServiceImpl.loadStock("AAPL");

        //Clear IntraDay, but leave status as true to check if update query works
        stock.setHasIntraDay(true);
        stock.setIntraDay(null);

        //Save stock to DB
        stockServiceImpl.saveStock(stock);

        //Update status
        stockServiceImpl.updateHasIntraDay("AAPL");

        //Fetch from DB
        Stock updatedStock = stockServiceImpl.getStock("AAPL");

        assertFalse(updatedStock.getHasIntraDay());
    }

    @Test
    @Order(4)
    @DisplayName("Update hasEndOfDay test.")
    void tickersUpdateHasEndOfDayTest() {
        //Clear DB
        stockServiceImpl.deleteAll();
        endOfDayServiceImpl.deleteAll();

        //Load stock
        Stock stock = stockServiceImpl.loadStock("AAPL");

        //Clear EOD, but leave status as true to check if update query works
        stock.setHasEndOfDay(true);
        stock.setEndOfDay(null);

        //Save stock to DB
        stockServiceImpl.saveStock(stock);

        //Update status
        stockServiceImpl.updateHasEndOfDay("AAPL");

        //Fetch from DB
        Stock updatedStock = stockServiceImpl.getStock("AAPL");

        assertFalse(updatedStock.getHasEndOfDay());

        isDoneTesting = true;
    }
}
