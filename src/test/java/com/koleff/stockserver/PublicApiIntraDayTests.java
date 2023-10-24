package com.koleff.stockserver;

import com.koleff.stockserver.remoteApi.service.impl.IntraDayPublicApiServiceImpl;
import com.koleff.stockserver.stocks.domain.*;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PublicApiIntraDayTests {
    private final IntraDayPublicApiServiceImpl intraDayPublicApiServiceImpl;
    private final IntraDayServiceImpl intraDayServiceImpl;
    private final StockServiceImpl stockServiceImpl;
    private final StockExchangeServiceImpl stockExchangeServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;
    private final TimezoneServiceImpl timezoneServiceImpl;

    @Autowired
    public PublicApiIntraDayTests(IntraDayPublicApiServiceImpl intraDayPublicApiServiceImpl,
                                  IntraDayServiceImpl intraDayServiceImpl,
                                  StockServiceImpl stockServiceImpl,
                                  StockExchangeServiceImpl stockExchangeServiceImpl,
                                  CurrencyServiceImpl currencyServiceImpl,
                                  TimezoneServiceImpl timezoneServiceImpl) {
        this.intraDayPublicApiServiceImpl = intraDayPublicApiServiceImpl;
        this.intraDayServiceImpl = intraDayServiceImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.stockExchangeServiceImpl = stockExchangeServiceImpl;
        this.currencyServiceImpl = currencyServiceImpl;
        this.timezoneServiceImpl = timezoneServiceImpl;
    }

    @BeforeEach
    public void setup() {
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
    }

    @Test
    @Order(1)
    void intraDayExportingTest() {
        //Fetch data and export it to JSONs
        intraDayPublicApiServiceImpl.exportAllDataToJson();

        //Load JSONs
        List<List<IntraDay>> intraDays = intraDayServiceImpl.loadAllIntraDays();

        Assertions.assertNotNull(intraDays);
    }


    @Test
    @Order(2)
    void intraDayLoadingTest() {
        //Check if V2 file is created...
        intraDayPublicApiServiceImpl.saveBulkData();

        List<List<IntraDayDto>> intraDayDtos = intraDayServiceImpl.getAllIntraDays();

        Assertions.assertNotNull(intraDayDtos);
    }
}
