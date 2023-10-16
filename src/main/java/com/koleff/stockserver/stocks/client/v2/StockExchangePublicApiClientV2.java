package com.koleff.stockserver.stocks.client.v2;

import com.koleff.stockserver.stocks.domain.StockExchange;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "stockExchangePublicApiClient", //Used for dependency injection
        url = "http://api.marketstack.com/v1/" //TODO: append
)
public interface StockExchangePublicApiClientV2 extends PublicApiClientV2<StockExchange>{ }
