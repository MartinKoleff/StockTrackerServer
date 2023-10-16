package com.koleff.stockserver.stocks.client.v2;

import com.koleff.stockserver.stocks.domain.Stock;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "stockPublicApiClient", //Used for dependency injection
        url = "http://api.marketstack.com/v1/" //TODO: append
)
public interface StockPublicApiClientV2 extends PublicApiClientV2<Stock>{ }
