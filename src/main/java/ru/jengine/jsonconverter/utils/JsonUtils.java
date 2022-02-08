package ru.jengine.jsonconverter.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class JsonUtils {
    public static boolean isLink(JsonElement value) {
        if (!value.isJsonPrimitive()) {
            return false;
        }
        JsonPrimitive primitive = value.getAsJsonPrimitive();
        return primitive.isString() && primitive.getAsString().contains(":");
    }
}
