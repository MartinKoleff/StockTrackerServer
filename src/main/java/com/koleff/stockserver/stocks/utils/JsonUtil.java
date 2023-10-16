package com.koleff.stockserver.stocks.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.domain.StockExchange;
import com.koleff.stockserver.stocks.domain.wrapper.DataWrapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

@Component
public class JsonUtil<T> {

    private final String resourcePath = "src/main/resources/json/%s";
    private Type type;

    /**
     * Stores parametrized type
     * Function is used because generics are not stored during runtime.
     * @param databaseTable type of request
     * @return type of parametrized type T
     */
    @SuppressWarnings("Generics")
    public static Type extractType(String databaseTable) {
        return switch (databaseTable) {
            case "intraday" -> new TypeToken<DataWrapper<IntraDay>>() {
            }.getType();
            case "eod" -> new TypeToken<DataWrapper<EndOfDay>>() {
            }.getType();
            case "exchange" -> new TypeToken<DataWrapper<StockExchange>>() {
            }.getType();
            case "tickers" -> new TypeToken<DataWrapper<Stock>>() {
            }.getType();
            default -> null;
        };
    }

    public void setType(Type type) {
        this.type = type;
    }

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
     *
     * @param json data in JSON format
     * @return json mapped to custom object T
     */
    public T convertJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * Saves data to JSON
     *
     * @param response    data
     * @param requestName remote api request name used for configuring JSON file name
     * @param stockTag    stock tag
     */
    public void exportToJson(T response, String requestName, String stockTag) {
        String filePath, jsonPath;

        //Create file path based on request
        switch (requestName) {
            case "intraday":
                jsonPath = String.format("intraday%s.json", stockTag);
                break;
            case "eod":
                jsonPath = String.format("eod%s.json", stockTag);
                break;
            case "exchange":
            case "tickers":
            default:
                return;
        }
        filePath = String.format(resourcePath, jsonPath);

        //Convert response to JSON
        String json;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            System.out.println("JSON could not be created.");
            return;
        }

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
