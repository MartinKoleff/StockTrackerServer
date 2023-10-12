package com.koleff.stockserver.stocks.client;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "publicApiClient", //Used for dependency injection
        url = "http://api.marketstack.com/v1/"
)
public interface PublicApiClientV2<T> {

    @GetMapping("{databaseTable}")
    DataWrapper<T> getData(@RequestParam String apiKey,
                          @RequestParam String stockTag,
                          @PathVariable("databaseTable") String databaseTable);
}
