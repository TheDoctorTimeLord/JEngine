package ru.jengine.jsonconverter.serializeprocess;

import com.google.gson.JsonSerializer;

public interface JsonConverterSerializer<T> extends JsonSerializer<T> {
    default boolean inHierarchy() {
        return false;
    }
}