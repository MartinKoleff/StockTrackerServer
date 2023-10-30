package com.koleff.stockserver.remoteApi.client.v2.base;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.dto.validation.DatabaseTableDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface PublicApiClientV2<T> {
    @GetMapping(value = "{request_name}")
    DataWrapper<T> getData(@PathVariable("request_name") DatabaseTableDto type,
                           @RequestParam(name = "access_key") String apiKey,
                           @RequestParam(name = "symbols", required = false) String stockTag);
}
