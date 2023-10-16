package com.koleff.stockserver.stocks.client.v2;

import com.koleff.stockserver.stocks.domain.IntraDay;
import org.junit.Before;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;

@FeignClient(
        value = "intraDayPublicApiClient", //Used for dependency injection
        url = "http://api.marketstack.com/v1/" //TODO: append
)
public interface IntraDayPublicApiClientV2 extends PublicApiClientV2<IntraDay>{ }
