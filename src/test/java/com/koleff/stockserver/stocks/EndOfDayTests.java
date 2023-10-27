package com.koleff.stockserver.stocks;

import com.koleff.stockserver.StockServerApplication;
import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.stocks.service.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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
public class EndOfDayTests {

    private final static Logger logger = LogManager.getLogger(EndOfDayTests.class);
    private final EndOfDayServiceImpl endOfDayServiceImpl;
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private boolean isDoneTesting = false;
    private boolean hasInitializedDB = false;

    private long startTime;
    private long endTime;
    private long totalTime;

    @Autowired
    EndOfDayTests(EndOfDayServiceImpl endOfDayServiceImpl,
                  StockServiceImpl stockServiceImpl,
                  StockExchangeServiceImpl stockExchangeServiceImpl,
                  CurrencyServiceImpl currencyServiceImpl,
                  TimezoneServiceImpl timezoneServiceImpl) {
        this.endOfDayServiceImpl = endOfDayServiceImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.currencyServiceImpl = currencyServiceImpl;
        this.timezoneServiceImpl = timezoneServiceImpl;
    }

    @BeforeEach
    public void setup() {
        if(hasInitializedDB){
            return;
        }
        logger.info("Setup before test starts...");

        //Load and Save stocks to DB
        List<Stock> stocks = stockServiceImpl.loadAllStocks();

        List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

        //Need to load and save stock_exchange before saving stock entity
        List<Currency> currencies = currencyServiceImpl.loadAllCurrencies();
        List<Timezone> timezones = timezoneServiceImpl.loadAllTimezones();
        List<StockExchange> stockExchanges = stockExchangeServiceImpl.loadAllStockExchanges();

        currencyServiceImpl.saveCurrencies(currencies);
        timezoneServiceImpl.saveTimezones(timezones);
        stockExchangeServiceImpl.saveStockExchanges(stockExchanges);

        stockServiceImpl.saveStocks(stocks);

        endOfDayServiceImpl.saveAllEndOfDays(eods);

        startTime = System.currentTimeMillis();

        hasInitializedDB = true;
    }

    @AfterEach
    public void tearDown() {
        endTime = System.currentTimeMillis() - startTime;
        totalTime = endTime - startTime;
        logger.info(String.format("Starting time: %d\n Finish time: %d\n Total time: %d", startTime, endTime, totalTime));

        if (!isDoneTesting) {
            logger.info("Testing finished!");
            return;
        }
        logger.info("Setup after test ends...");
        logger.info("Deleting all DB entries...");

        //Clear the DB
        currencyServiceImpl.deleteAll();
        timezoneServiceImpl.deleteAll();
        stockExchangeServiceImpl.deleteAll();
        stockServiceImpl.deleteAll();
        endOfDayServiceImpl.deleteAll();

        boolean isDBEmpty = stockServiceImpl.getStocks().isEmpty()
                && endOfDayServiceImpl.getAllEndOfDays().isEmpty()
                && stockExchangeServiceImpl.getStockExchanges().isEmpty()
                && currencyServiceImpl.getCurrencies().isEmpty()
                && timezoneServiceImpl.getTimezones().isEmpty();

        logger.info(String.format("DB is empty: %s", isDBEmpty));
    }

    @Test
    @Order(1)
    @DisplayName("Fetching data from DB.")
    void eodFetchingTest() {
        List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

        logger.info(String.format("All EOD DTOs from DB: %s", eodDtos));
        Assertions.assertNotNull(eodDtos);
    }

    @Test
    @Order(2)
    @DisplayName("Loading data from JSON.")
    void eodLoadingTest() {
        List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

        logger.info(String.format("All EODs loaded from all JSONs: %s", eods));
        Assertions.assertNotNull(eods);

    }

    @Test
    @Order(3)
    @DisplayName("Fetching 1 entry from DB.")
    void eodFetchingOneEntryTest() {
        String stockTag = "AAPL";

        List<EndOfDayDto> eodDto = endOfDayServiceImpl.getEndOfDay(stockTag);

        logger.info(String.format("EOD DTO for %s stock: %s", stockTag, eodDto));
        Assertions.assertNotNull(eodDto);

    }

    @Test
    @Order(4)
    @DisplayName("Saving all data from JSON to DB.")
    void eodBulkSavingTest() { //TODO: add Spring Batch
        //Clear DB
        endOfDayServiceImpl.deleteAll();

        //Load data from JSON
        List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

        //Save data to DB
        endOfDayServiceImpl.saveAllEndOfDays(eods);

        //Check if entries are in DB
        List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

        logger.info(String.format("All EOD DTOs from DB: %s", eodDtos));
        Assertions.assertNotNull(eodDtos);
    }

    @Test
    @Order(5)
    @DisplayName("Saving 1 entry from JSON to DB.")
    void eodSavingOneEntryTest(){
        //Clear DB
        endOfDayServiceImpl.deleteAll();

        String stockTag = "AAPL";

        //Load data from JSON
        List<EndOfDay> eod = endOfDayServiceImpl.loadEndOfDay(stockTag);

        //Save data to DB
        endOfDayServiceImpl.saveEndOfDay(eod);

        //Check if entries are in DB
        List<EndOfDayDto> eodDto = endOfDayServiceImpl.getEndOfDay(stockTag);

        logger.info(String.format("EOD DTO for %s stock: %s", stockTag, eodDto));
        Assertions.assertNotNull(eodDto);

        isDoneTesting = true;
    }
}
