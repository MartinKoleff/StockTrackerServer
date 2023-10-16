package com.koleff.stockserver.stocks.client.v2;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "endOfDayPublicApiClient", //Used for dependency injection
        url = "http://api.marketstack.com/v1/" //TODO: append
)
public interface EndOfDayPublicApiClientV2 extends PublicApiClientV2<EndOfDay>{
}
