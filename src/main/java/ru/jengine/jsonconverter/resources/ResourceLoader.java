package ru.jengine.jsonconverter.resources;

import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;

public interface ResourceLoader {
    String getResource(ResourceMetadata metadata) throws ResourceLoadingException;
    boolean hasResourceByMetadata(ResourceMetadata metadata);

    void addCacheListener(CacheChangingListener listener);
    void addResourceToCache(ResourceMetadata metadata, String resource);
    void cleanCache();

    enum CacheChangeType { ADD, REPLACE, DELETE }
    @FunctionalInterface
    interface CacheChangingListener {
        void changed(ResourceMetadata metadata, CacheChangeType changeType);
    }
}
