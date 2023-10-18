package com.koleff.stockserver;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.service.impl.IntraDayServiceImpl;
import org.apache.logging.log4j.Logger;
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
public class IntraDayTests {
    private final IntraDayServiceImpl intraDayServiceImpl;

    @Qualifier("logger")
    private final Logger logger;

    @Autowired
    IntraDayTests(IntraDayServiceImpl intraDayServiceImpl,
                  Logger logger) {
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.logger = logger;
    }

    @Test
    @Order(2)
    void intraDaysLoadingTest() {
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        intraDayServiceImpl.saveAllIntraDays(intraDays);

        Assertions.assertNotNull(intraDayServiceImpl.getAllIntraDays());
    }
}
