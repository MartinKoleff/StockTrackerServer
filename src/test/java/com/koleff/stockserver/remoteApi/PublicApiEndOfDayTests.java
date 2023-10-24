package com.koleff.stockserver.remoteApi;

import com.koleff.stockserver.StockServerApplication;
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
    }

    @AfterEach
    public void tearDown() {
        if (isDoneTesting){
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
    void eodExportingTest() {
        //Fetch data and export it to JSONs
        endOfDayPublicApiServiceImpl.exportAllDataToJson();

        //Load JSONs
        List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

        Assertions.assertNotNull(eods);
    }

    @Test
    @Order(2)
    void eodFetchingTest() {
        List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

        Assertions.assertNotNull(eodDtos);
    }

    @Test
    @Order(3)
    void eodLoadingTest() {
        List<List<EndOfDay>> intraDays = endOfDayServiceImpl.loadAllEndOfDays();

        Assertions.assertNotNull(intraDays);
    }

    @Test
    @Order(4)
    void eodBulkSavingTest() {
        //Save to DB
        endOfDayPublicApiServiceImpl.saveBulkData();

        //Check if entries are in DB
        List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

        Assertions.assertNotNull(eodDtos);

        isDoneTesting = true;
    }
}
