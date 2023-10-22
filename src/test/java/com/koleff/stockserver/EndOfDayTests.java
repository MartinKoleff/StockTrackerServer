package com.koleff.stockserver;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.service.impl.EndOfDayServiceImpl;
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
        webEnvironment = RANDOM_PORT
)
@TestInstance(PER_CLASS)
@ContextConfiguration(
        classes = {StockServerApplication.class, TestConfiguration.class}
)
@ExtendWith(SpringExtension.class)
public class EndOfDayTests {
    private final EndOfDayServiceImpl endOfDayServiceImpl;

    @Qualifier("logger")
    private final Logger logger;
    private boolean isDoneTesting = false; //To use with @AfterAll

    @Autowired
    EndOfDayTests(EndOfDayServiceImpl endOfDayServiceImpl,
                  Logger logger) {
        this.endOfDayServiceImpl = endOfDayServiceImpl;
        this.logger = logger;
    }

    @Before
    public void setup() {
        logger.info("Setup before test starts...");
        logger.info("Deleting all DB entries...");

        endOfDayServiceImpl.deleteAll();
        boolean isDBEmpty = endOfDayServiceImpl.getAllEndOfDays().isEmpty();
        logger.info("DB is empty: %s", isDBEmpty);
    }

    @After
    public void tearDown() {
        if (isDoneTesting){
            logger.info("Testing finished!");
            return;
        }

        logger.info("Deleting all DB entries...");
        endOfDayServiceImpl.deleteAll();

        boolean isDBEmpty = endOfDayServiceImpl.getAllEndOfDays().isEmpty();
        logger.info("DB is empty: %s", isDBEmpty);
    }

    @Test
    @Order(1)
    void endOfDaysLoadingTest() {
        List<List<EndOfDay>> eods = endOfDayServiceImpl.loadAllEndOfDays();

        endOfDayServiceImpl.saveAllEndOfDays(eods);

        List<List<EndOfDayDto>> eodDtos = endOfDayServiceImpl.getAllEndOfDays();
        logger.info(eodDtos);

        Assertions.assertNotNull(eodDtos);

        isDoneTesting = true;
    }
}
