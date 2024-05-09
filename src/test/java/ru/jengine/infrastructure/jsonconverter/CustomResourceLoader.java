package ru.jengine.infrastructure.jsonconverter;

import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;
import ru.jengine.jsonconverter.resources.ResourceLoaderWithCache;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

public class CustomResourceLoader extends ResourceLoaderWithCache {
    @Override
    public boolean hasResourceByMetadata(ResourceMetadata metadata) {
        try {
            getResource(metadata);
            return true;
        } catch (ResourceLoadingException e) {
            return false;
        }
    }

    @Override
    protected String getResourceInt(ResourceMetadata metadata) throws ResourceLoadingException {
        throw new ResourceLoadingException("Can not read resources [%s]".formatted(metadata));
    }
}