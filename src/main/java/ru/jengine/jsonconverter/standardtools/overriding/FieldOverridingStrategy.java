package ru.jengine.jsonconverter.standardtools.overriding;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@FunctionalInterface
public interface FieldOverridingStrategy {
    boolean override(JsonObject targetObject, String fieldName, JsonElement overridingValue);
}
