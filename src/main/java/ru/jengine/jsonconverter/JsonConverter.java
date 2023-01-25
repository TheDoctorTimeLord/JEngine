package ru.jengine.jsonconverter;

import ru.jengine.jsonconverter.resources.ResourceMetadata;

import com.google.gson.JsonElement;

public interface JsonConverter {
    JsonElement convertToJson(Object converingObject);

    <T> T convertFromJson(ResourceMetadata resource, Class<T> convertTo);
    <T> T convertFromJson(String json, Class<T> convertTo);
}
