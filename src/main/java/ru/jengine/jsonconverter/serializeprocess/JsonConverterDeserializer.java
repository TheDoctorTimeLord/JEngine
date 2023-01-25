package ru.jengine.jsonconverter.serializeprocess;

import com.google.gson.JsonDeserializer;

public interface JsonConverterDeserializer<T> extends JsonDeserializer<T> {
    default boolean inHierarchy() {
        return false;
    }
}
