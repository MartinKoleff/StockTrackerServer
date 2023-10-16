package com.koleff.stockserver.remoteApi.client.v2.base;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "publicApiClient", //Used for dependency injection
        url = "${spring.cloud.openfeign.client.config.postClient.url}"
)
public interface PublicApiClientV2<T> {
    DataWrapper<T> getData(@RequestParam String apiKey,
                           @RequestParam String stockTag);
}
