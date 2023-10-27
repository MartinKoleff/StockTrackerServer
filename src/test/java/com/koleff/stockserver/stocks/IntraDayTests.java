package com.koleff.stockserver.stocks;

import com.koleff.stockserver.StockServerApplication;
import com.koleff.stockserver.stocks.domain.*;
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
public class IntraDayTests {

    private final static Logger logger = LogManager.getLogger(IntraDayTests.class);
    private final IntraDayServiceImpl intraDayServiceImpl;
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private boolean isDoneTesting = false;
    private long startTime;
    private long endTime;
    private long totalTime;

    @Autowired
    IntraDayTests(IntraDayServiceImpl intraDayServiceImpl,
                  StockServiceImpl stockServiceImpl,
                  StockExchangeServiceImpl stockExchangeServiceImpl,
                  CurrencyServiceImpl currencyServiceImpl,
                  TimezoneServiceImpl timezoneServiceImpl) {
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

        startTime = System.currentTimeMillis();
    }

    //    @AfterEach
    public void tearDown() {
        endTime = System.currentTimeMillis() - startTime;
        totalTime = endTime - startTime;
        logger.info(String.format("Starting time: %d\n Finish time: %d\n Total time: %d", startTime, endTime, totalTime));

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
    @DisplayName("Fetching data from DB.")
    void intraDayFetchingTest() {
        List<List<IntraDayDto>> intraDayDtos = intraDayServiceImpl.getAllIntraDays();

        logger.info(String.format("All IntraDay DTOs from DB: %s", intraDayDtos));
        Assertions.assertNotNull(intraDayDtos);
    }

    @Test
    @Order(2)
    @DisplayName("Loading data from JSON.")
    void intraDayLoadingTest() {
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        logger.info(String.format("All IntraDays loaded from all JSONs: %s", intraDays));
        Assertions.assertNotNull(intraDays);
    }


    @Test
    @Order(3)
    @DisplayName("Fetching 1 entry from DB.")
    void intraDayFetchingOneEntryTest() {
        String stockTag = "AAPL";

        List<IntraDayDto> intraDayDto = intraDayServiceImpl.getIntraDay(stockTag);

        logger.info(String.format("IntraDay DTO for %s stock: %s", stockTag, intraDayDto));
        Assertions.assertNotNull(intraDayDto);
    }

    @Test
    @Order(4)
    @Disabled("To optimize...")
    @DisplayName("Saving all data from JSON to DB.")
    void intraDayBulkSavingTest(){
        //Clear DB
        intraDayServiceImpl.deleteAll();

        //Load data from JSON
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        //Save data to DB
        intraDayServiceImpl.saveAllIntraDays(intraDays);

        //Check if entries are in DB
        List<List<IntraDayDto>> intraDayDtos = intraDayServiceImpl.getAllIntraDays();

        logger.info(String.format("All IntraDay DTOs from DB: %s", intraDayDtos));
        Assertions.assertNotNull(intraDayDtos);

    }

    @Test
    @Order(5)
    @DisplayName("Saving 1 entry from JSON to DB.")
    void intraDaySavingOneEntryTest(){
        //Clear DB
        intraDayServiceImpl.deleteAll();

        String stockTag = "AAPL";

        //Load data from JSON
        List<IntraDay> intraDays = intraDayServiceImpl.loadIntraDay(stockTag);

        //Save data to DB
        intraDayServiceImpl.saveIntraDay(intraDays);

        //Check if entries are in DB
        List<IntraDayDto> intraDayDto = intraDayServiceImpl.getIntraDay(stockTag);

        logger.info(String.format("IntraDay DTO for %s stock: %s", stockTag, intraDayDto));
        Assertions.assertNotNull(intraDayDto);

        isDoneTesting = true;
    }
}
