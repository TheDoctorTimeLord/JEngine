package ru.jengine.jsonconverter.resources;

import ru.jengine.jsonconverter.exceptions.JsonLoaderException;
import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;

import com.google.gson.JsonElement;

public interface JsonLoader {
    JsonElement getJson(ResourceMetadata metadata) throws JsonLoaderException, ResourceLoadingException;

    JsonElement addResourceToCache(ResourceMetadata metadata, JsonElement resource);
    boolean hasInCache(ResourceMetadata metadata);
    void cleanCache();
}
