package com.koleff.stockserver.remoteApi.client.v2.base;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface PublicApiClientV2<T> {

    @GetMapping("get")
    DataWrapper<T> getData(@RequestParam String apiKey,
                           @RequestParam String stockTag);
}
