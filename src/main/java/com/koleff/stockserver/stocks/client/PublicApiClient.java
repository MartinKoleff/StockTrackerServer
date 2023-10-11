package com.koleff.stockserver.stocks.client;

import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import com.koleff.stockserver.stocks.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PublicApiClient<T> {
    private final String url = "http://api.marketstack.com/v1/";

    @Value("${apiKey}")
    private String apiKey;
    private final JsonUtil jsonUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public PublicApiClient(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    public DataWrapper<T> getData(String databaseTable, String stockTag) {
        String requestUrl = String.format("%s%s?access_key=%s&symbols=%s",
                url,
                databaseTable,
                apiKey,
                stockTag);

        //Call remote API
        DataWrapper<T> response = restTemplate.getForObject(requestUrl, DataWrapper.class);
        return response;
    }

    public void saveDataToJSON(String databaseTable, String stockTag) {
        String requestUrl = String.format("%s%s?access_key=%s&symbols=%s",
                url,
                databaseTable,
                apiKey,
                stockTag);

        //Call remote API
        DataWrapper<T> response = restTemplate.getForObject(requestUrl, DataWrapper.class);

        //Export to JSON
        jsonUtil.setType(JsonUtil.extractType(databaseTable));
        jsonUtil.exportToJson(response, databaseTable, stockTag);
    }
}
