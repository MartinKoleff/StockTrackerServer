package com.koleff.stockserver.stocks.client.v2;

import com.koleff.stockserver.stocks.domain.Stock;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "stockPublicApiClient", //Used for dependency injection
        url = "${spring.cloud.openfeign.client.config.postClient.url}"
)
public interface StockPublicApiClientV2 extends PublicApiClientV2<Stock>{ }
