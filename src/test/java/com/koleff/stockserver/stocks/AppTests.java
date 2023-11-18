package com.koleff.stockserver.stocks;

import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.*;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.stocks.service.impl.*;
import com.koleff.stockserver.stocks.utils.stockExchangesUtil.StockExchangesUtil;
import com.koleff.stockserver.stocks.utils.tickersUtil.TickersUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
public class AppTests {
    private final static Logger logger = LogManager.getLogger(AppTests.class);
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private final EndOfDayServiceImpl endOfDayServiceImpl;
    private final IntraDayServiceImpl intraDayServiceImpl;
    private final TickersUtil tickersUtil;
    private final StockExchangesUtil stockExchangesUtil;
    private boolean isDoneTesting = false;
    private boolean hasInitializedDB = false;
    private long startTime;
    private long endTime;
    private long totalTime;

    @Autowired
    AppTests(StockServiceImpl stockServiceImpl,
             StockExchangeServiceImpl stockExchangeServiceImpl,
             CurrencyServiceImpl currencyServiceImpl,
             TimezoneServiceImpl timezoneServiceImpl,
             EndOfDayServiceImpl endOfDayServiceImpl,
             IntraDayServiceImpl intraDayServiceImpl,
             TickersUtil tickersUtil,
             StockExchangesUtil stockExchangesUtil) {
        this.stockServiceImpl = stockServiceImpl;
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.currencyServiceImpl = currencyServiceImpl;
        this.timezoneServiceImpl = timezoneServiceImpl;
        this.endOfDayServiceImpl = endOfDayServiceImpl;
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.tickersUtil = tickersUtil;
        this.stockExchangesUtil = stockExchangesUtil;
    }

    @BeforeEach
    public void setup() {
        logger.info("Setup before test starts...");

        boolean isDBEmpty = stockServiceImpl.getStocks().isEmpty()
                && intraDayServiceImpl.getAllIntraDays().isEmpty()
                && endOfDayServiceImpl.getAllEndOfDays().isEmpty()
                && stockExchangeServiceImpl.getStockExchanges().isEmpty()
                && currencyServiceImpl.getCurrencies().isEmpty()
                && timezoneServiceImpl.getTimezones().isEmpty();

        logger.info(String.format("DB is empty: %s", isDBEmpty));

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
        logger.info("Setup after test ends...");

        endTime = System.currentTimeMillis() - startTime;
        totalTime = endTime - startTime;
        logger.info(String.format("Starting time: %d\n Finish time: %d\n Total time: %d", startTime, endTime, totalTime));

        logger.info("Deleting all DB entries...");
    }

    /**
     * Testcontainer setup
     */

    @LocalServerPort
    private Integer port;

    @Value("spring.datasource.username")
    private static String usernameDB;

    @Value("spring.datasource.password")
    private static String passwordDB;
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = (PostgreSQLContainer<?>) new PostgreSQLContainer
            ("postgres:16.0")
            .withDatabaseName("stocks")
            .withUsername(usernameDB)
            .withPassword(passwordDB)
            .withReuse(false);


    @DynamicPropertySource
    //TODO: use 1 class for all tests (This annotation doesn't work with JUnit 5 @ExtendWith() -> manually add for each test class...)
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        postgreSQLContainer.stop();
    }

    /**
     * Tests
     */
    @Nested
    class TickersTests {

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
        @Disabled("Exports to JSON functionality will change current V2 JSON files...")
            //TODO: add test profile to determine if export or no / delete files / use new naming for testing
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
        }
    }

    @Nested
    class StockExchangeTests {
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
        }
    }

    @Nested
    class EndOfDayTests {
        @Test
        @Order(1)
        @DisplayName("Fetching data from DB.")
            //TODO: test with FetchType.Lazy...
        void eodFetchingTest() {
            List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

            logger.info(String.format("All EOD DTOs from DB: %s", eodDtos));

            assertAll(
                    "Validation of EOD fetching data from DB.",
                    () -> assertNotNull(eodDtos),
                    () -> assertFalse(eodDtos.isEmpty())
            );
        }

        @Test
        @Order(2)
        @DisplayName("Loading data from JSON.")
        void eodLoadingTest() {
            List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

            logger.info(String.format("All EODs loaded from all JSONs: %s", eods));

            assertAll(
                    "Validation of EOD loading data from JSON.",
                    () -> assertNotNull(eods),
                    () -> assertFalse(eods.isEmpty())
            );

        }

        @Test
        @Order(3)
        @DisplayName("Fetching 1 entry from DB.")
        void eodFetchingOneEntryTest() {
            String stockTag = "AAPL";

            List<EndOfDayDto> eodDtos = endOfDayServiceImpl.getEndOfDays(stockTag);

            logger.info(String.format("EOD DTO for %s stock: %s", stockTag, eodDtos));

            assertAll(
                    "Validation of EOD for 1 stock fetching data from DB.",
                    () -> assertNotNull(eodDtos),
                    () -> assertFalse(eodDtos.isEmpty())
            );
        }

        @Test
        @Order(4)
        @DisplayName("Saving all data from JSON to DB.")
        void eodBulkSavingTest() {
            //Clear DB
            endOfDayServiceImpl.deleteAll();

            //Load data from JSON
            List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

            //Save data to DB
            endOfDayServiceImpl.saveAllEndOfDays(eods);

            //Check if entries are in DB
            List<List<EndOfDayDto>> allEodDtos = endOfDayServiceImpl.getAllEndOfDays();

            logger.info(String.format("All EOD DTOs from DB: %s", allEodDtos));

            assertAll(
                    "Validation of EOD fetching data from DB after saving it from JSON loading.",
                    () -> assertNotNull(allEodDtos),
                    () -> assertFalse(allEodDtos.isEmpty())
            );
        }

        @Test
        @Order(5)
        @DisplayName("Saving 1 entry from JSON to DB.")
        void eodSavingOneEntryTest() {
            //Clear DB
            endOfDayServiceImpl.deleteAll();

            String stockTag = "AAPL";

            //Load data from JSON
            List<EndOfDay> eod = endOfDayServiceImpl.loadEndOfDays(stockTag);

            //Save data to DB
            endOfDayServiceImpl.saveEndOfDay(eod);

            //Check if entries are in DB
            List<EndOfDayDto> eodDto = endOfDayServiceImpl.getEndOfDays(stockTag);

            logger.info(String.format("EOD DTO for %s stock: %s", stockTag, eodDto));

            assertAll(
                    "Validation of EOD for 1 stock fetching data from DB after saving.",
                    () -> assertNotNull(eodDto),
                    () -> assertFalse(eodDto.isEmpty())
            );
        }

        @Test
        @Order(6)
        @Sql("/schema/schema-postgresql.sql") //Execute if Spring Batch default tables are not created for you.
        @DisplayName("Saving all entries via Spring Batch")
        void eodSavingViaSpringBatch() {
            //Clear DB before test
            endOfDayServiceImpl.deleteAll();

            //Save bulk entries
            endOfDayServiceImpl.saveViaJob();

            //Check if entries are in DB
            List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

            logger.info(String.format("All EOD DTOs from DB: %s", eodDtos));
            logger.info(String.format("All EOD DTOs size %d", eodDtos.size()));

            assertAll(
                    "Validation of EOD fetching data from DB after saving it via SPRING BATCH from JSON loading.",
                    () -> assertNotNull(eodDtos),
                    () -> assertFalse(eodDtos.isEmpty())
            );
        }

        @Test
        @Order(7)
        @DisplayName("Date filtration with dateFrom and dateTo.")
        void eodDateFiltration() {
            String stockTag = "AAPL";
            String dateFrom = "2023-10-24T00:00:00+00:00";
            String dateTo = "2023-10-25T00:00:00+00:00";
            List<EndOfDayDto> eods = endOfDayServiceImpl.getEndOfDays(stockTag, dateFrom, dateTo);

            Assertions.assertEquals(2, eods.size());
        }

        @Test
        @Order(8)
        @DisplayName("Date filtration with date.")
        void eodDateFiltration2() {
            String stockTag = "AAPL";
            String date = "2023-10-26T00:00:00+00:00";
            EndOfDayDto eod = endOfDayServiceImpl.getEndOfDay(stockTag, date);

            Assertions.assertNotNull(eod);
        }
    }

    @Nested
    class IntraDayTests {
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
        void intraDaySavingViaSpringBatch() {
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
        void intraDayDateFiltration() {
            String stockTag = "AAPL";
            String dateFrom = "2023-10-24T00:00:00+00:00";
            String dateTo = "2023-10-25T00:00:00+00:00";
            List<IntraDayDto> intraDayDtos = intraDayServiceImpl.getIntraDays(stockTag, dateFrom, dateTo);

            Assertions.assertEquals(2, intraDayDtos.size());
        }

        @Test
        @Order(8)
        @DisplayName("Date filtration with date.")
        void intraDayDateFiltration2() {
            String stockTag = "AAPL";
            String date = "2023-10-26T00:00:00+00:00";
            IntraDayDto intraDayDto = intraDayServiceImpl.getIntraDay(stockTag, date);

            Assertions.assertNotNull(intraDayDto);
            isDoneTesting = true;
        }
    }
}
