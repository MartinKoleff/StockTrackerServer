package com.koleff.stockserver.remoteApi;

import com.koleff.stockserver.StockServerApplication;
import com.koleff.stockserver.stocks.EndOfDayTests;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.remoteApi.service.impl.IntraDayPublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.service.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
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
public class PublicApiIntraDayTests {

    private final static Logger logger = LogManager.getLogger(PublicApiIntraDayTests.class);
    private final IntraDayPublicApiServiceImpl intraDayPublicApiServiceImpl;
    private final IntraDayServiceImpl intraDayServiceImpl;
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private boolean isDoneTesting = false;

    @Autowired
    public PublicApiIntraDayTests(IntraDayPublicApiServiceImpl intraDayPublicApiServiceImpl,
                                  IntraDayServiceImpl intraDayServiceImpl,
                                  StockServiceImpl stockServiceImpl,
                                  StockExchangeServiceImpl stockExchangeServiceImpl,
                                  CurrencyServiceImpl currencyServiceImpl,
                                  TimezoneServiceImpl timezoneServiceImpl) {
        this.intraDayPublicApiServiceImpl = intraDayPublicApiServiceImpl;
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.currencyServiceImpl = currencyServiceImpl;
        this.timezoneServiceImpl = timezoneServiceImpl;
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
        intraDayServiceImpl.deleteAll();

        boolean isDBEmpty = stockServiceImpl.getStocks().isEmpty()
                && intraDayServiceImpl.getAllIntraDays().isEmpty()
                && stockExchangeServiceImpl.getStockExchanges().isEmpty()
                && currencyServiceImpl.getCurrencies().isEmpty()
                && timezoneServiceImpl.getTimezones().isEmpty();

        logger.info(String.format("DB is empty: %s", isDBEmpty));
    }

    @Test
    @Order(1)
    @Disabled("Does 100 requests traffic to remote API. You only have 1000 requests limit.")
    @DisplayName("Fetching data from remote API and exporting it to JSON.")
    void intraDayExportingTest() {
        //Fetch data and export it to JSONs
        intraDayPublicApiServiceImpl.exportAllDataToJson();

        //Load JSONs
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        Assertions.assertNotNull(intraDays);
    }


    @Test
    @Order(2)
    @DisplayName("Fetching data from DB.")
    void intraDayFetchingTest() {
        List<List<IntraDayDto>> intraDayDtos = intraDayServiceImpl.getAllIntraDays();

        Assertions.assertNotNull(intraDayDtos);
    }

    @Test
    @Order(3)
    @DisplayName("Loading data from JSON.")
    void intraDayLoadingTest() {
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        Assertions.assertNotNull(intraDays);
    }

    @Test
    @Order(4)
    @DisplayName("Saving all data from JSON to DB.")
    void intraDayBulkSavingTest(){
        //Save to DB
        intraDayPublicApiServiceImpl.saveBulkData();

        //Check if entries are in DB
        List<List<IntraDayDto>> intraDayDtos = intraDayServiceImpl.getAllIntraDays();

        Assertions.assertNotNull(intraDayDtos);

        isDoneTesting = true;
    }
}
