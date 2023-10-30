package com.koleff.stockserver.remoteApi;

import com.koleff.stockserver.StockServerApplication;
import com.koleff.stockserver.remoteApi.service.impl.IntraDayPublicApiServiceImpl;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.remoteApi.service.impl.EndOfDayPublicApiServiceImpl;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.domain.*;
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
public class PublicApiEndOfDayTests {

    private final static Logger logger = LogManager.getLogger(PublicApiEndOfDayTests.class);
    private final EndOfDayPublicApiServiceImpl endOfDayPublicApiServiceImpl;
    private final EndOfDayServiceImpl endOfDayServiceImpl;
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private boolean isDoneTesting = false;
    private boolean hasInitializedDB = false;

    @Autowired
    public PublicApiEndOfDayTests(EndOfDayPublicApiServiceImpl endOfDayPublicApiServiceImpl,
                                  EndOfDayServiceImpl endOfDayServiceImpl,
                                  StockServiceImpl stockServiceImpl,
                                  StockExchangeServiceImpl stockExchangeServiceImpl,
                                  CurrencyServiceImpl currencyServiceImpl,
                                  TimezoneServiceImpl timezoneServiceImpl) {
        this.endOfDayPublicApiServiceImpl = endOfDayPublicApiServiceImpl;
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

        hasInitializedDB = true;
    }

    @AfterEach
    public void tearDown() {
        if (!isDoneTesting){
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
    @Disabled("Does 100 requests traffic to remote API. You only have 1000 requests limit.")
    @DisplayName("Fetching data from remote API and exporting it to JSON.")
    void eodExportingTest() {
        //Fetch data and export it to JSONs
        endOfDayPublicApiServiceImpl.exportAllDataToJson();

        //Load JSONs
        List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

        Assertions.assertNotNull(eods);
    }

    @Test
    @Order(2)
    @Disabled("Used to investigate bug with fetching IntraDay data for EOD client...")
    @DisplayName("Fetching 1 entry from remote API and exporting it to JSON.")
    void eodFetchDataFromRemoteAPIOneEntryTest() {
        String stockTag = "AAPL";

        List<EndOfDay> entry = endOfDayPublicApiServiceImpl.getData(stockTag).getData();

        Assertions.assertNotNull(entry);
    }

    @Test
    @Order(3)
    @DisplayName("Fetching data from DB.")
    void eodFetchingTest() {
        List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

        Assertions.assertNotNull(eodDtos);
    }

    @Test
    @Order(4)
    @DisplayName("Loading data from JSON.")
    void eodLoadingTest() {
        List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

        Assertions.assertNotNull(eods);
    }

    @Test
    @Order(5)
    @DisplayName("Saving all data from JSON to DB.")
    void eodBulkSavingTest() {
        //Save to DB
        endOfDayPublicApiServiceImpl.saveBulkData();

        //Check if entries are in DB
        List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

        Assertions.assertNotNull(eodDtos);

        isDoneTesting = true;
    }
}
