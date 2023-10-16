package com.koleff.stockserver.remoteApi.client.v2;

import com.koleff.stockserver.stocks.domain.IntraDay;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "intraDayPublicApiClient", //Used for dependency injection
        url = "${spring.cloud.openfeign.client.config.postClient.url}"
)
public interface IntraDayPublicApiClientV2 extends PublicApiClientV2<IntraDay>{ }
