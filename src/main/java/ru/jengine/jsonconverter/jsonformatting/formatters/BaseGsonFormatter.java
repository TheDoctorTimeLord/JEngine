package ru.jengine.jsonconverter.jsonformatting.formatters;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class BaseGsonFormatter implements JsonFormatter<JsonObject> {
    @Override
    public JsonObject correct(JsonObject jsonObject) {
        return jsonObject;
    }

    @Override
    public List<String> extractJsonDependencies(JsonObject jsonObject) {
        return Collections.emptyList();
    }

    @Override
    public boolean formatting(JsonObject mainJson, Map<String, JsonObject> dependencies) {
        boolean wasChanged = false;

        //TODO костыль. См JsonFormattersManagerGson
        for (String dependency : extractJsonDependencies(mainJson)) {
            JsonElement jsonElement = mainJson.get(dependency);
            if (jsonElement == null || !jsonElement.isJsonPrimitive()) {
                continue;
            }

            String link = jsonElement.getAsJsonPrimitive().getAsString();
            JsonObject jsonObject = dependencies.get(link);
            if (jsonObject == null) {
                continue;
            }

            mainJson.add(dependency, jsonObject);
            wasChanged = true;
        }

        return wasChanged;
    }

    @Override
    public void cleanResultObject(JsonObject resultObject) { }
}
