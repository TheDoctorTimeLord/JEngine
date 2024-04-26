package ru.jengine.jsonconverter.resources;

import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ResourceLoaderWithCache implements ResourceLoader {
    private final Map<ResourceMetadata, String> resourcesCache = new ConcurrentHashMap<>();
    private final List<CacheChangingListener> cacheListeners = new CopyOnWriteArrayList<>();

    @Override
    public String getResource(ResourceMetadata metadata) throws ResourceLoadingException {
        String cached = resourcesCache.get(metadata);
        if (cached != null) {
            return cached;
        }

        //Не cached.computeIfAbsent(...) из-за throws у getResourceInt
        String resource = getResourceInt(metadata);
        addResourceToCache(metadata, resource);
        return resource;
    }

    protected abstract String getResourceInt(ResourceMetadata metadata) throws ResourceLoadingException;

    @Override
    public void addCacheListener(CacheChangingListener listener) {
        cacheListeners.add(listener);
    }

    @Override
    public void addResourceToCache(ResourceMetadata metadata, String resource) {
        if (resourcesCache.put(metadata, resource) != null) {
            notifyListeners(metadata, CacheChangeType.REPLACE);
        }
        notifyListeners(metadata, CacheChangeType.ADD);
    }

    @Override
    public void cleanCache() {
        for (ResourceMetadata metadata : resourcesCache.keySet()) {
            notifyListeners(metadata, CacheChangeType.DELETE);
        }
        resourcesCache.clear();
    }

    private void notifyListeners(ResourceMetadata metadata, CacheChangeType changeType) {
        for (CacheChangingListener listener : cacheListeners) {
            listener.changed(metadata, changeType);
        }
    }
}
