package com.koleff.stockserver.stocks;

import com.koleff.stockserver.StockServerApplication;
import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.StockDto;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.stocks.resources.TestResources;
import com.koleff.stockserver.stocks.service.impl.*;
import com.koleff.stockserver.stocks.utils.tickersUtil.TickersUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Using VM options to configure PostgreSQL DB login
 */
@SpringBootTest(
        webEnvironment = RANDOM_PORT //TODO: look up
)
@TestInstance(PER_CLASS)
@ContextConfiguration(
        classes = {StockServerApplication.class, TestConfiguration.class}
)
@ExtendWith(SpringExtension.class) //TODO: look up
public class TickersTests {
    private final static Logger logger = LogManager.getLogger(TickersTests.class);

    @ClassRule
    public static final TestResources res = new TestResources();
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;
    private final EndOfDayServiceImpl endOfDayServiceImpl;
    private final IntraDayServiceImpl intraDayServiceImpl;
    private final TickersUtil tickersUtil;
    private boolean isInitialized = false;
    private boolean isDoneTesting = false;

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

    }

    @AfterEach
    public void tearDown() {
        if (isDoneTesting){
            logger.info("Testing finished!");
            return;
        }
        logger.info("Setup after test ends...");
        logger.info("Deleting all DB entries...");

        stockServiceImpl.deleteAll();
        intraDayServiceImpl.deleteAll();
        endOfDayServiceImpl.deleteAll();
        stockExchangeServiceImpl.deleteAll();
        currencyServiceImpl.deleteAll();
        timezoneServiceImpl.deleteAll();

        boolean isDBEmpty = stockServiceImpl.getStocks().isEmpty()
                && intraDayServiceImpl.getAllIntraDays().isEmpty()
                && endOfDayServiceImpl.getAllEndOfDays().isEmpty()
                && stockExchangeServiceImpl.getStockExchanges().isEmpty()
                && currencyServiceImpl.getCurrencies().isEmpty()
                && timezoneServiceImpl.getTimezones().isEmpty();

        logger.info(String.format("DB is empty: %s", isDBEmpty));
    }

    @Test
    @Order(1)
    void tickersLoadingTest() {
        List<Stock> stocks = stockServiceImpl.loadAllStocks();

        //Need to load and save stock_exchange before saving stock entity
        List<Currency> currencies = currencyServiceImpl.loadAllCurrencies();
        List<Timezone> timezones = timezoneServiceImpl.loadAllTimezones();
        List<StockExchange> stockExchanges = stockExchangeServiceImpl.loadAllStockExchanges();

        currencyServiceImpl.saveCurrencies(currencies);
        timezoneServiceImpl.saveTimezones(timezones);
        stockExchangeServiceImpl.saveStockExchanges(stockExchanges);

//        //Saving stocks to use with DB data...
//        stockServiceImpl.saveStocks(stocks);

        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();
        List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

        //Saving order matters!
        intraDayServiceImpl.saveAllIntraDays(intraDays);
        endOfDayServiceImpl.saveAllEndOfDays(eods);
        stockServiceImpl.saveStocks(stocks);

        List<StockDto> stockDtos = stockServiceImpl.getStocks();
        logger.debug(stockDtos);

        Assertions.assertNotNull(stockDtos);
    }


    @Test
    @Order(2)
    void tickersConfigureStockExchangeIdTest() {
        tickersUtil.configureIds();

        List<Stock> stocks = stockServiceImpl.loadAllStocks();

        Assertions.assertFalse(stocks.stream()
                .map(Stock::getStockExchangeId)
                .anyMatch(Objects::isNull)
        );

        isDoneTesting = true;
    }
}
