package com.koleff.stockserver;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.service.impl.IntraDayServiceImpl;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
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
public class IntraDayTests {
    private final IntraDayServiceImpl intraDayServiceImpl;

    @Qualifier("logger")
    private final Logger logger;
    private boolean isDoneTesting = false; //To use with @AfterAll

    @Autowired
    IntraDayTests(IntraDayServiceImpl intraDayServiceImpl,
                  Logger logger) {
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.logger = logger;
    }

    @BeforeEach
    public void setup() {
        logger.info("Setup before test starts...");
        logger.info("Deleting all DB entries...");

        intraDayServiceImpl.deleteAll();
        boolean isDBEmpty = intraDayServiceImpl.getAllIntraDays().isEmpty();
        logger.info("DB is empty: %s", isDBEmpty);
    }

    @AfterEach
    public void tearDown() {
        if (isDoneTesting){
            logger.info("Testing finished!");
            return;
        }

        logger.info("Deleting all DB entries...");
        intraDayServiceImpl.deleteAll();

        boolean isDBEmpty = intraDayServiceImpl.getAllIntraDays().isEmpty();
        logger.info("DB is empty: %s", isDBEmpty);
    }

    @Test
    @Order(1)
    void intraDaysLoadingTest() {
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        intraDayServiceImpl.saveAllIntraDays(intraDays);

        List<List<IntraDayDto>> intraDayDtos = intraDayServiceImpl.getAllIntraDays();
        logger.info(intraDayDtos);

        Assertions.assertNotNull(intraDayDtos);

        isDoneTesting = true;
    }
}
