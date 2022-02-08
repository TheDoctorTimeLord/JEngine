package ru.jengine.jsonconverter.jsonformatting.formatters;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        return false;
    }

    @Override
    public void cleanResultObject(JsonObject resultObject) {

    }
}
