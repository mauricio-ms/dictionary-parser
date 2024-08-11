package com.lang.trainer.dictionaries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

class DictionariesMerger {

    public static void main(String[] args) throws IOException {
        Type typeObject = new TypeToken<Dictionary>() {
        }.getType();
        Map<String, TreeSet<String>> frequencyDict = new Gson()
                .fromJson(new JsonReader(new FileReader("frequency.json")), typeObject);
        Map<String, TreeSet<String>> routledgeDict = new Gson()
                .fromJson(new JsonReader(new FileReader("routledge.json")), typeObject);
        Map<String, TreeSet<String>> mergedDict = new HashMap<>(routledgeDict);
        frequencyDict.forEach(mergedDict::putIfAbsent);

        JsonElement jsonData = new Gson().toJsonTree(mergedDict, typeObject);
        JsonObject jsonDict = new JsonObject();
        jsonDict.addProperty("timestamp", LocalDateTime.now().toString());
        jsonDict.add("data", jsonData);

        try (FileWriter writer = new FileWriter("dictionary.json")) {
            new GsonBuilder().create().toJson(jsonDict, writer);
        }
    }
}
