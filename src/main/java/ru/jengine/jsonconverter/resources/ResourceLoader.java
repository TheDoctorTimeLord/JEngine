package ru.jengine.jsonconverter.resources;

import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;

public interface ResourceLoader {
    String getResource(ResourceMetadata metadata) throws ResourceLoadingException;

    boolean hasResourceByMetadata(ResourceMetadata metadata);

    String addResourceToCache(ResourceMetadata metadata, String resource);
    void cleanCache();
}
