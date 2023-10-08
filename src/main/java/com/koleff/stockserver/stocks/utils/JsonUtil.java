package com.koleff.stockserver.stocks.utils;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonUtil<T> {

    private final Class<T> type;

    public JsonUtil(Class<T> type) {
        this.type = type;
    }

    public String getJson(String jsonPath) {
        String filePath = String.format("src/main/resources/json/%s", jsonPath);
        File jsonFile = new File(filePath);

        String json = null;
        try {
            JSONParser parser = new JSONParser();

            JSONObject data = (JSONObject) parser.parse(
                    new FileReader(jsonFile)
            );

             json = data.toJSONString();
        } catch (IOException | ParseException e) {
            System.out.println("Failed to parse JSON.");
        }

        return json;
    }

    public T convertJson(String json){
        Gson gson = new Gson();
//        Type collectionType = new TypeToken<T>(){}.getType();
        return gson.fromJson(json, type);
    }
}
