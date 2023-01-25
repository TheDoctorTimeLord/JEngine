package ru.jengine.jsonconverter.formatting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface JsonFormatterManager {
    boolean formatJson(JsonObject json);

    default boolean formatJson(JsonElement jsonElement) {
        boolean canBeCached = true;

        if (jsonElement.isJsonObject()) {
            canBeCached = formatJson(jsonElement.getAsJsonObject());
        }
        else if (jsonElement.isJsonArray()) {
            for (JsonElement jsonInArray : jsonElement.getAsJsonArray()) {
                if (jsonInArray.isJsonObject()) {
                    canBeCached = formatJson(jsonInArray.getAsJsonObject()) && canBeCached;
                }
            }
        }

        return canBeCached;
    }
}
