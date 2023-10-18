package com.koleff.stockserver.stocks.utils.jsonUtil.base;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public abstract class JsonUtil<T> {

    private final String resourcePath = "src/main/resources/json/%s";

    @Qualifier("gson")
    private final Gson gson;

    @Autowired
    protected JsonUtil(Gson gson) {
        this.gson = gson;
    }

    /**
     * Used to parse JSON to class T
     *
     * @return parametrized type T initialized in different child classes
     */
    protected abstract Type getType();

    /**
     * Load JSON from file
     *
     * @param jsonPath path to JSON file with data
     * @return json data
     */
    public String loadJson(String jsonPath) {
        String filePath = String.format(resourcePath, jsonPath);
        File jsonFile = new File(filePath);

        String json = null;
        try {
            JSONParser parser = new JSONParser();

            JSONObject data = (JSONObject) parser.parse(
                    new FileReader(jsonFile)
            );

            json = data.toJSONString();
        } catch (IOException | ParseException e) {
            System.out.printf("Failed to parse JSON. File exists -> %s\n", jsonFile.exists());
        }

        return json;
    }

    /**
     * Map JSON to custom object
     * - SerializedName() only works for serialization.
     * - For deserialization @JsonAlias from jackson library is supported by Spring.
     *
     * @param json data in JSON format
     * @return json mapped to custom object T
     */
    public T convertJson(String json) {
        return gson.fromJson(json, getType());
    }

    /**
     * Saves data to JSON
     * Used for IntraDay and EndOfDay entities
     *
     * @param response    data
     * @param requestName remote api request name used for configuring JSON file name
     * @param stockTag    stock tag
     */
    public void exportToJson(T response, String requestName, String stockTag) {
        String jsonPath;

        //Create file path based on request
        switch (requestName) {
            case "intraday":
                jsonPath = String.format("intraday%s.json", stockTag);
                break;
            case "eod":
                jsonPath = String.format("eod%s.json", stockTag);
                break;
            default:
                return;
        }

        export(response, jsonPath);
    }

    /**
     * Saves data to JSON
     * Used for Stock and StockExchange entities
     * - V2 suffix means configured JSON files
     *
     * @param response    data
     * @param requestName remote api request name used for configuring JSON file name
     */
    public void exportToJson(T response, String requestName) {
        String jsonPath;

        //Create file path based on request
        switch (requestName) {
            case "exchange":
                jsonPath = "exchanges.json";
                break;
            case "tickers":
                jsonPath = "tickers.json";
                break;
            case "tickersV2":
                jsonPath = "tickersV2.json";
                break;
            case "exchangeV2":
                jsonPath = "exchangesV2.json";
                break;
            default:
                return;
        }

        export(response, jsonPath);
    }

    /**
     * Saves data to JSON. Writes to file
     *
     * @param response data
     * @param jsonPath file path
     */
    private void export(T response, String jsonPath) {
        String filePath = String.format(resourcePath, jsonPath);

        //Convert response to JSON
        String json;
        json = gson.toJson(response);

        //Write JSON in file
        File jsonFile = new File(filePath);
        try {
            FileWriter file = new FileWriter(jsonFile);
            file.write(json);
            file.close();
        } catch (IOException e) {
            System.out.println("JSON could not be exported to file.");
            return;
        }

        System.out.printf("JSON file created: %s\n", json);
    }
}
