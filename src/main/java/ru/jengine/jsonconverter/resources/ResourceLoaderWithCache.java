package ru.jengine.jsonconverter.resources;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;

public abstract class ResourceLoaderWithCache implements ResourceLoader {
    private final Map<ResourceMetadata, String> resourcesCache = new ConcurrentHashMap<>();

    @Override
    public String getResource(ResourceMetadata metadata) throws ResourceLoadingException {
        String cached = resourcesCache.get(metadata);
        if (cached != null) {
            return cached;
        }

        //Не cached.computeIfAbsent(...) из-за throws у getResourceInt
        return addResourceToCache(metadata, getResourceInt(metadata));
    }

    protected abstract String getResourceInt(ResourceMetadata metadata) throws ResourceLoadingException;

    @Override
    public String addResourceToCache(ResourceMetadata metadata, String resource) {
        return resourcesCache.put(metadata, resource);
    }

    @Override
    public void cleanCache() {
        resourcesCache.clear();
    }
}
