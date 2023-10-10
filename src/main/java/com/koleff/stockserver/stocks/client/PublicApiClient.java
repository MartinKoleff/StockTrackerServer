package com.koleff.stockserver.stocks.client;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PublicApiClient {
    private final String url = "http://api.marketstack.com/v1/";

    @Value("${apiKey}")
    private String apiKey;
    private final JsonUtil jsonUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public PublicApiClient(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    public DataWrapper<IntraDay> getIntraDay(String stockTag) {
        String intraDayUrl = String.format("%sintraday?access_key=%s&symbols=%s",
                url,
                apiKey,
                stockTag);

        //Call remote API
        DataWrapper<IntraDay> response = restTemplate.getForObject(intraDayUrl, DataWrapper.class);
        return response;
    }

    public void saveIntraDayToJSON(String stockTag) {
        String intraDayUrl = String.format("%sintraday?access_key=%s&symbols=%s",
                url,
                apiKey,
                stockTag);

        //Call remote API
        DataWrapper<IntraDay> response = restTemplate.getForObject(intraDayUrl, DataWrapper.class);

        //Export to JSON
        jsonUtil.setType(DataWrapper.class);
        jsonUtil.exportToJson(response, "intraday", stockTag);
    }
}
