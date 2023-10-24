package com.koleff.stockserver.stocks.utils.jsonUtil.base;

import com.google.gson.Gson;
import com.koleff.stockserver.remoteApi.service.impl.base.PublicApiServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public abstract class JsonUtil<T> {

    private final static Logger logger = LogManager.getLogger(JsonUtil.class);
    private final String resourcePath = "src/main/resources/json/%s";

    @Qualifier("gson")
    private final Gson gson;

    @Autowired
    public JsonUtil(Gson gson) {
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
        logger.info("Loading JSON...");

        String filePath = String.format(resourcePath, jsonPath);
        logger.info(String.format("Json file path: %s", jsonPath));

        File jsonFile = new File(filePath);

        String json = null;
        try {
            JSONParser parser = new JSONParser();

            JSONObject data = (JSONObject) parser.parse(
                    new FileReader(jsonFile)
            );

            json = data.toJSONString();
        } catch (IOException | ParseException e) {
            logger.error(String.format("Failed to parse JSON. File exists -> %s\n", jsonFile.exists()));
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
        logger.info("Converting JSON...");

        T entity =  gson.fromJson(json, getType());

        logger.info(String.format("JSON was parsed to: %s", entity.toString()));
        return entity;
    }

    /**
     * Saves data to JSON
     * Used for IntraDay and EndOfDay entities
     *
     * @param response    data
     * @param requestName remote api request name used for configuring JSON file name
     * @param versionAnnotation which JSON file to use (V2 is with configured ids)
     * @param stockTag    stock tag
     */
    public void exportToJson(T response, String requestName, String versionAnnotation, String stockTag) {
        String jsonPath;

        //Create file path based on request
        switch (requestName) {
            case "intraday":
                jsonPath = String.format("intraday%s%s.json", stockTag, versionAnnotation);
                break;
            case "eod":
                jsonPath = String.format("eod%s%s.json", stockTag, versionAnnotation);
                break;
            default:
                return;
        }

        logger.info(String.format("Json name: %s", jsonPath));
        export(response, jsonPath);
    }

    /**
     * Saves data to JSON
     * Used for Stock and StockExchange entities
     * - V2 suffix means configured JSON files
     *
     * @param response    data
     * @param requestName remote api request name used for configuring JSON file name
     * @param versionAnnotation which JSON file to use (V2 is with configured ids)
     */
    public void exportToJson(T response, String requestName, String versionAnnotation) {
        String jsonPath;

        //Create file path based on request
        switch (requestName) {
            case "exchanges":
                jsonPath =  String.format("exchanges%s.json", versionAnnotation);
                break;
            case "tickers":
                jsonPath = String.format("tickers%s.json", versionAnnotation);
                break;
            default:
                return;
        }

        logger.info(String.format("Json name: %s", jsonPath));
        export(response, jsonPath);
    }

    /**
     * Saves data to JSON. Writes to file
     *
     * <p> When using Jackson library for JSON converting:
     * <p> - serialize data with JsonProperty() instead of SerializedName()
     * <p> - use ObjectMapper() for converting instead of Gson
     * <p> - Gson is not compatible with Jackson.
     * 
     * @param response data
     * @param jsonPath file path
     */
    private void export(T response, String jsonPath) {
        logger.info("Exporting data to JSON...");

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
            logger.error("JSON could not be exported to file.");
            return;
        }

        logger.info(String.format("JSON file successfully created! JSON file content: %s\n", json));
    }
}
