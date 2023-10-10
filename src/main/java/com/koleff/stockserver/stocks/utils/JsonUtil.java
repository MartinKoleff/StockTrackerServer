package com.koleff.stockserver.stocks.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.koleff.stockserver.stocks.domain.IntraDay;
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

    private Type type;
    private final String resourcePath = "src/main/resources/json/%s";

    //Generics are created on build time. They are lost on run time.
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Load JSON from file
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
     */
    public T convertJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * Saves data to JSON
     */
    public void exportToJson(T response, String requestName, String stockTag) { //To automate requestName and stockTag to fetch from response...
        String filePath;

        //Create file path based on request
        switch (requestName) {
            case "intraday":
                String jsonPath = String.format("intraday%s.json", stockTag);
                filePath = String.format(resourcePath, jsonPath);
                break;
            default:
                return;
        }

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
