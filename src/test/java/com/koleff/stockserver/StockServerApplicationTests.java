package com.koleff.stockserver;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.service.impl.IntraDayServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockExchangeServiceImpl;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
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
class StockServerApplicationTests {

    private final StockServiceImpl stockServiceImpl;
    private final IntraDayServiceImpl intraDayServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    @Qualifier("logger")
    private final Logger logger;
    private boolean isInitialized = false;
    private boolean isDoneTesting = false;

    @Autowired
    StockServerApplicationTests(StockServiceImpl stockServiceImpl,
                                IntraDayServiceImpl intraDayServiceImpl,
                                StockExchangeServiceImpl stockExchangeServiceImpl,
                                Logger logger) {
        this.stockServiceImpl = stockServiceImpl;
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.logger = logger;
    }

    @Before//@BeforeClass
    public void setup() {
        if (isInitialized) return;
        stockServiceImpl.deleteAll();

        logger.info("Creating DB connection");
        logger.info("Adding tickers to DB");

        //Load tickers from JSON
        List<Stock> stocks = stockServiceImpl.loadAllStocks();
        isInitialized = !stocks.isEmpty();
        logger.info("Tickers are loaded: %s", isInitialized);

        //Save tickers to DB
        stockServiceImpl.saveStocks(stocks);
    }

    @After //@AfterClass
    public void tearDown() {
        if (isDoneTesting) return;

        logger.info("Closing DB connection...");
        logger.info("Deleting all DB entries...");

        stockServiceImpl.deleteAll();
        boolean isDBEmpty = stockServiceImpl.getStocks().isEmpty();
        logger.info("DB is empty: %s", isDBEmpty);
    }

    @Test
    @Order(1)
    void tickersLoadingTest() {
        stockServiceImpl.deleteAll();

        List<Stock> stocks = stockServiceImpl.loadAllStocks();

        stockServiceImpl.saveStocks(stocks);

        Assertions.assertNotNull(stockServiceImpl.getStocks());
    }

    @Test
    @Order(2)
    void intradaysLoadingTest() {
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        intraDayServiceImpl.saveAllIntraDays(intraDays);

        Assertions.assertNotNull(intraDayServiceImpl.getAllIntraDays());
    }

    @Test
    @Order(3)
    void stockExchangesLoadingTest() {
        List<StockExchange> stockExchanges = stockExchangeServiceImpl.loadAllStockExchanges();

        stockExchangeServiceImpl.saveStockExchanges(stockExchanges);

        Assertions.assertNotNull(stockExchangeServiceImpl.getStockExchanges());

        isDoneTesting = true;
    }
}
