package ru.jengine.jsonconverter.standardtools;

import com.google.gson.JsonDeserializer;

public interface JsonConverterDeserializer<T> extends JsonDeserializer<T> {
    default boolean inHierarchy() {
        return false;
    }
}
