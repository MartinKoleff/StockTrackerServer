package com.koleff.stockserver.stocks;

import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.stocks.service.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Using VM options to configure PostgreSQL DB login
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(
        classes = {TestConfiguration.class}
)
@Testcontainers
//@TestInstance(PER_CLASS)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@TestExecutionListeners({DirtiesContextTestExecutionListener.class})
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional(propagation = Propagation.REQUIRES_NEW)
public class EndOfDayTests {

    private final static Logger logger = LogManager.getLogger(EndOfDayTests.class);
    private final EndOfDayServiceImpl endOfDayServiceImpl;
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
//    private MockMvc mockMvc;
    private boolean isDoneTesting = false;
    private boolean hasInitializedDB = false;

    private long startTime;
    private long endTime;
    private long totalTime;

    /**
     * Test container setup
     */

    @LocalServerPort
    private Integer port;

    @Value("spring.datasource.username") //TODO: wire with VM Options...
    private static String usernameDB;

    @Value("spring.datasource.password")
    private static String passwordDB;


    @Container
//    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = (PostgreSQLContainer<?>) new PostgreSQLContainer
            ("postgres:16.0")
            .withDatabaseName("stocks")
            .withUsername("postgres")
            .withPassword("")
            .withReuse(true);

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    public EndOfDayTests(EndOfDayServiceImpl endOfDayServiceImpl,
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

    //    @Commit
    @BeforeEach
    public void setup() {
        logger.info("Setup before test starts...");

        logger.info(String.format("Testcontainer JDBC URL: %s", postgreSQLContainer.getJdbcUrl()));

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

    //    @Commit
    @AfterEach
    public void tearDown() {
        endTime = System.currentTimeMillis() - startTime;
        totalTime = endTime - startTime;
        logger.info(String.format("Starting time: %d\n Finish time: %d\n Total time: %d", startTime, endTime, totalTime));

        if (isDoneTesting) {
            logger.info("Testing finished!");
            return;
        }
        logger.info("Setup after test ends...");
        logger.info("Deleting all DB entries...");

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

        List<EndOfDayDto> eodDtos = endOfDayServiceImpl.getEndOfDays(stockTag);

        logger.info(String.format("EOD DTO for %s stock: %s", stockTag, eodDtos));
        Assertions.assertNotNull(eodDtos);

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
        Assertions.assertNotNull(allEodDtos);
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
        Assertions.assertNotNull(eodDto);
    }

    @Test
    @Order(6)
    @Sql("/schema/schema-postgresql.sql")
    @DisplayName("Saving all entries via Spring Batch")
    void eodSavingViaSpringBatch() {
        endOfDayServiceImpl.saveViaJob();

        //Check if entries are in DB
        List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();

        logger.info(String.format("All EOD DTOs from DB: %s", eodDtos));
        logger.info(String.format("All EOD DTOs size %d", eodDtos.size()));
        Assertions.assertNotNull(eodDtos);
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
        isDoneTesting = true;
    }
}
