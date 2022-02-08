package ru.jengine.jsonconverter;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.converting.ConverterToObject;
import ru.jengine.jsonconverter.exceptions.JsonConverterRuntimeException;
import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;
import ru.jengine.jsonconverter.jsonformatting.JsonFormatterModule;
import ru.jengine.jsonconverter.resources.ResourceLoader;
import ru.jengine.jsonconverter.resources.ResourceLoaderWithCache;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

@Bean
public class JsonConverterImpl implements JsonConverter {
    private final ResourceLoader resourceLoader;
    private final JsonFormatterModule jsonFormatter;
    private final ConverterToObject converter;

    public JsonConverterImpl(@Nullable ResourceLoader resourceLoader, JsonFormatterModule jsonFormatter,
            ConverterToObject converter) {
        this.resourceLoader = resourceLoader == null ? new DefaultResourceLoader() : resourceLoader;
        this.jsonFormatter = jsonFormatter;
        this.converter = converter;
    }

    @Override
    public <T> T convertFromJson(ResourceMetadata metadata, Class<T> convertedResultClass)
            throws JsonConverterRuntimeException
    {
        try {
            return convertFromJson(resourceLoader.getResource(metadata), convertedResultClass);
        }
        catch (ResourceLoadingException e) {
            throw new JsonConverterRuntimeException(String.format("Resource by [%s] was not loaded for [%s]",
                    metadata, convertedResultClass), e);
        }
    }

    @Override
    public <T> T convertFromJson(String json, Class<T> convertedResultClass) throws JsonConverterRuntimeException {
        JsonContext jsonContext = jsonFormatter.formatJson(json, resourceLoader, true);
        return converter.convertToObject(jsonContext, convertedResultClass);
    }

    public static class DefaultResourceLoader extends ResourceLoaderWithCache {
        @Override
        public String getResourceInt(ResourceMetadata metadata) throws ResourceLoadingException {
            throw new ResourceLoadingException("DefaultResourceLoader can not load resources");
        }
    }
}
