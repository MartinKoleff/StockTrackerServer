package com.koleff.stockserver.stocks.client.v2;

import com.koleff.stockserver.stocks.domain.StockExchange;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "stockExchangePublicApiClient", //Used for dependency injection
        url = "${spring.cloud.openfeign.client.config.postClient.url}"
)
public interface StockExchangePublicApiClientV2 extends PublicApiClientV2<StockExchange>{ }
