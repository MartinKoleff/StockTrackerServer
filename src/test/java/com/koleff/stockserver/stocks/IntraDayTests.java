package com.koleff.stockserver.stocks;

import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.resources.DatabaseSetupExtension;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.stocks.service.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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
public class IntraDayTests {

    private final static Logger logger = LogManager.getLogger(IntraDayTests.class);
    private final IntraDayServiceImpl intraDayServiceImpl;
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

        assertAll(
                "Validation of intra day fetching data from DB.",
                () -> assertNotNull(intraDayDtos),
                () -> assertFalse(intraDayDtos.isEmpty())
        );
    }

    @Test
    @Order(2)
    @DisplayName("Loading data from JSON.")
    void intraDayLoadingTest() {
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        logger.info(String.format("All IntraDays loaded from all JSONs: %s", intraDays));

        assertAll(
                "Validation of intra day loading data from JSON.",
                () -> assertNotNull(intraDays),
                () -> assertFalse(intraDays.isEmpty())
        );
    }


    @Test
    @Order(3)
    @DisplayName("Fetching 1 entry from DB.")
    void intraDayFetchingOneEntryTest() {
        String stockTag = "AAPL";

        List<IntraDayDto> intraDayDto = intraDayServiceImpl.getIntraDays(stockTag);

        logger.info(String.format("IntraDay DTO for %s stock: %s", stockTag, intraDayDto));

        assertAll(
                "Validation of intra day for 1 stock fetching data from DB.",
                () -> assertNotNull(intraDayDto),
                () -> assertFalse(intraDayDto.isEmpty())
        );
    }

    @Test
    @Order(4)
    @Disabled("To optimize...")
    @DisplayName("Saving all data from JSON to DB.")
    void intraDayBulkSavingTest() {
        //Clear DB
        intraDayServiceImpl.deleteAll();

        //Load data from JSON
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        //Save data to DB
        intraDayServiceImpl.saveAllIntraDays(intraDays);

        //Check if entries are in DB
        List<List<IntraDayDto>> allIntraDayDtos = intraDayServiceImpl.getAllIntraDays();

        logger.info(String.format("All IntraDay DTOs from DB: %s", allIntraDayDtos));

        assertAll(
                "Validation of intra day fetching data from DB after saving it from JSON loading.",
                () -> assertNotNull(allIntraDayDtos),
                () -> assertFalse(allIntraDayDtos.isEmpty())
        );
    }

    @Test
    @Order(5)
    @DisplayName("Saving 1 entry from JSON to DB.")
    void intraDaySavingOneEntryTest() {
        //Clear DB
        intraDayServiceImpl.deleteAll();

        String stockTag = "AAPL";

        //Load data from JSON
        List<IntraDay> intraDays = intraDayServiceImpl.loadIntraDays(stockTag);

        //Save data to DB
        intraDayServiceImpl.saveIntraDay(intraDays);

        //Check if entries are in DB
        List<IntraDayDto> intraDayDto = intraDayServiceImpl.getIntraDays(stockTag);

        logger.info(String.format("IntraDay DTO for %s stock: %s", stockTag, intraDayDto));

        assertAll(
                "Validation of intra day for 1 stock fetching data from DB after saving.",
                () -> assertNotNull(intraDayDto),
                () -> assertFalse(intraDayDto.isEmpty())
        );
    }

    @Test
    @Order(6)
    @Sql("/schema/schema-postgresql.sql") //Execute if Spring Batch default tables are not created for you.
    @DisplayName("Saving all entries via Spring Batch")
    void intraDaySavingViaSpringBatch(){
        //Clear DB before test
        intraDayServiceImpl.deleteAll();

        //Save bulk entries
        intraDayServiceImpl.saveViaJob();

        //Check if entries are in DB
        List<List<IntraDayDto>> intraDayDtos = intraDayServiceImpl.getAllIntraDays();

        logger.info(String.format("All IntraDay DTOs from DB: %s", intraDayDtos));
        logger.info(String.format("All IntraDay DTOs size %d", intraDayDtos.size()));

        assertAll(
                "Validation of intra day fetching data from DB after saving it via SPRING BATCH from JSON loading.",
                () -> assertNotNull(intraDayDtos),
                () -> assertFalse(intraDayDtos.isEmpty())
        );
    }

    @Test
    @Order(7)
    @DisplayName("Date filtration with dateFrom and dateTo.")
    void intraDayDateFiltration(){
        String stockTag = "AAPL";
        String dateFrom = "2023-10-24T00:00:00+00:00";
        String dateTo = "2023-10-25T00:00:00+00:00";
        List<IntraDayDto> intraDayDtos = intraDayServiceImpl.getIntraDays(stockTag, dateFrom, dateTo);

        Assertions.assertEquals(2, intraDayDtos.size());
    }

    @Test
    @Order(8)
    @DisplayName("Date filtration with date.")
    void intraDayDateFiltration2(){
        String stockTag = "AAPL";
        String date = "2023-10-26T00:00:00+00:00";
        IntraDayDto intraDayDto = intraDayServiceImpl.getIntraDay(stockTag, date);

        Assertions.assertNotNull(intraDayDto);
        isDoneTesting = true;
    }
}
