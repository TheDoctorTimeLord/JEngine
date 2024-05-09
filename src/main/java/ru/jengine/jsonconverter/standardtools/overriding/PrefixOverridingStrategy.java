package ru.jengine.jsonconverter.standardtools.overriding;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public abstract class PrefixOverridingStrategy implements FieldOverridingStrategy {
    private final String prefix;

    protected PrefixOverridingStrategy(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean override(JsonObject targetObject, String fieldName, JsonElement overridingValue) {
        String targetField = prefix + fieldName;
        if (!targetObject.has(targetField))
        {
            return false;
        }

        if (handleFields(targetObject, fieldName, targetObject.get(targetField), overridingValue)) {
            targetObject.remove(targetField);
            return true;
        }
        return false;
    }

    private boolean handleFields(JsonObject target, String fieldName, JsonElement targetValue, JsonElement overridingValue) {
        if (targetValue.isJsonArray() && overridingValue.isJsonArray()) {
            return arrayValue(target, fieldName, targetValue.getAsJsonArray(), overridingValue.getAsJsonArray());
        }
        if (targetValue.isJsonObject() && overridingValue.isJsonObject()) {
            return objectValue(target, fieldName, targetValue.getAsJsonObject(), overridingValue.getAsJsonObject());
        }
        if (targetValue.isJsonPrimitive() && overridingValue.isJsonPrimitive()) {
            return primitiveValue(target, fieldName, targetValue.getAsJsonPrimitive(), overridingValue.getAsJsonPrimitive());
        }
        return false;
    }

    protected boolean arrayValue(JsonObject target, String field, JsonArray targetValue, JsonArray overridingValue) {
        return false;
    }

    protected boolean objectValue(JsonObject target, String field, JsonObject targetValue, JsonObject overridingValue) {
        return false;
    }

    protected boolean primitiveValue(JsonObject target, String field, JsonPrimitive targetValue, JsonPrimitive overridingValue) {
        return false;
    }
}
