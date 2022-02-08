package ru.jengine.jsonconverter;

import ru.jengine.jsonconverter.exceptions.JsonConverterRuntimeException;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

public interface JsonConverter {
    <T> T convertFromJson(ResourceMetadata metadata, Class<T> convertedResultClass) throws JsonConverterRuntimeException;
    <T> T convertFromJson(String json, Class<T> convertedResultClass) throws JsonConverterRuntimeException;
}
