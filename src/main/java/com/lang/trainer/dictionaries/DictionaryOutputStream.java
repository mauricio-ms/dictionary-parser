package com.lang.trainer.dictionaries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;

public final class DictionaryOutputStream {
    private final Dictionary dictionary;
    private final JsonElement jsonData;

    public DictionaryOutputStream(Dictionary dictionary) {
        this.dictionary = dictionary;

        Gson gson = new Gson();
        Type typeObject = new TypeToken<Dictionary>() {
        }.getType();
        jsonData = gson.toJsonTree(dictionary, typeObject);
    }

    public void write() throws IOException {
        write(dictionary.filePath(), new GsonBuilder());
        write(dictionary.prettyPrintFilePath(), new GsonBuilder().setPrettyPrinting());
    }

    private void write(Path filePath, GsonBuilder gsonBuilder) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toString())) {
            gsonBuilder.create().toJson(jsonData, writer);
        }
    }
}
