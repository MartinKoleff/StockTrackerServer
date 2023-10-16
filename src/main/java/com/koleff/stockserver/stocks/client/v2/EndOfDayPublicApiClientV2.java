package com.koleff.stockserver.stocks.client.v2;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "endOfDayPublicApiClient", //Used for dependency injection
        url = "${spring.cloud.openfeign.client.config.postClient.url}"
)
public interface EndOfDayPublicApiClientV2 extends PublicApiClientV2<EndOfDay>{
}
