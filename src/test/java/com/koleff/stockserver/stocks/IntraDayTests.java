package com.koleff.stockserver.stocks;

import com.koleff.stockserver.StockServerApplication;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.resources.TestConfiguration;
import com.koleff.stockserver.stocks.service.impl.IntraDayServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private final static Logger logger = LogManager.getLogger(IntraDayTests.class);
    private final IntraDayServiceImpl intraDayServiceImpl;
    private boolean isDoneTesting = false;

    @Autowired
    IntraDayTests(IntraDayServiceImpl intraDayServiceImpl) {
        this.intraDayServiceImpl = intraDayServiceImpl;
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
        intraDayServiceImpl.deleteAll();

        boolean isDBEmpty = intraDayServiceImpl.getAllIntraDays().isEmpty();
        logger.info(String.format("DB is empty: %s", isDBEmpty));
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
