package com.koleff.stockserver;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.service.IntraDayService;
import com.koleff.stockserver.stocks.service.StockExchangeService;
import com.koleff.stockserver.stocks.service.StockService;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        webEnvironment = RANDOM_PORT
)
@TestInstance(PER_CLASS)
@ContextConfiguration(
        classes = {StockServerApplication.class, TestConfiguration.class}
)
@ExtendWith(SpringExtension.class)
/**
 * Using VM options to configure PostgreSQL DB login
 */
class StockServerApplicationTests {

    private final StockService stockService;
    private final IntraDayService intraDayService;
    private final StockExchangeService stockExchangeService;
    @Qualifier("logger")
    private final Logger logger;
    private boolean isInitialized = false;
    private boolean isDoneTesting = false;

    @Autowired
    StockServerApplicationTests(StockService stockService,
                                IntraDayService intraDayService,
                                StockExchangeService stockExchangeService,
                                Logger logger) {
        this.stockService = stockService;
        this.intraDayService = intraDayService;
        this.stockExchangeService = stockExchangeService;
        this.logger = logger;
    }

    @Before//@BeforeClass
    public void setup() {
        if (isInitialized) return;
        stockService.deleteAll();

        logger.info("Creating DB connection");
        logger.info("Adding tickers to DB");

        //Load tickers from JSON
        List<Stock> stocks = stockService.loadStocks();
        isInitialized = !stocks.isEmpty();
        logger.info("Tickers are loaded: %s", isInitialized);

        //Save tickers to DB
        stockService.saveStocks(stocks);
    }

    @After //@AfterClass
    public void tearDown() {
        if (isDoneTesting) return;

        logger.info("Closing DB connection...");
        logger.info("Deleting all DB entries...");

        stockService.deleteAll();
        boolean isDBEmpty = stockService.getStocks().isEmpty();
        logger.info("DB is empty: %s", isDBEmpty);
    }

    @Test
    @Order(1)
    void tickersLoadingTest() {
        stockService.deleteAll();

        List<Stock> stocks = stockService.loadStocks();

        stockService.saveStocks(stocks);

        Assertions.assertNotNull(stockService.getStocks());
    }

    @Test
    @Order(2)
    void intradaysLoadingTest() {
        List<List<IntraDay>> intraDays = intraDayService.loadAllIntraDays();

        intraDayService.saveAllIntraDay(intraDays);

        Assertions.assertNotNull(intraDayService.getAllIntraDays());
    }

    @Test
    @Order(3)
    void stockExchangesLoadingTest() {
        List<StockExchange> stockExchanges = stockExchangeService.loadStockExchanges();

        stockExchangeService.saveStockExchanges(stockExchanges);

        Assertions.assertNotNull(stockExchangeService.getStockExchanges());

        isDoneTesting = true;
    }
}
