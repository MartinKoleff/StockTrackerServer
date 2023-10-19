package com.koleff.stockserver;

import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.service.impl.StockServiceImpl;
import com.koleff.stockserver.stocks.utils.tickersUtil.TickersUtil;
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
public class TickersTests { //TODO: have one main test class and run all test classes...
    private final StockServiceImpl stockServiceImpl;
    private final TickersUtil tickersUtil;


    @Qualifier("logger")
    private final Logger logger;
    private boolean isInitialized = false;
    private boolean isDoneTesting = false;

    @Autowired
    TickersTests(StockServiceImpl stockServiceImpl,
                 TickersUtil tickersUtil, Logger logger) {
        this.stockServiceImpl = stockServiceImpl;
        this.tickersUtil = tickersUtil;
        this.logger = logger;
    }

    @Before//@BeforeClass //TODO: move to classRule class
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

    @After //@AfterClass //TODO: move to classRule class
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
    void tickersConfigureStockExchangeIdTest() {
        tickersUtil.configureStockExchangeId();

        List<Stock> stocks = stockServiceImpl.loadAllStocks();

        Assertions.assertFalse(stocks.stream()
                .map(Stock::getStockExchangeId)
                .anyMatch(Objects::isNull)
        );
    }
}
