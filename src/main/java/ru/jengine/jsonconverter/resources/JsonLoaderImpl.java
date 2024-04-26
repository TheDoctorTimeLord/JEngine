package ru.jengine.jsonconverter.resources;

import com.google.gson.JsonElement;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.exceptions.JsonLoaderException;
import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;
import ru.jengine.jsonconverter.resources.ResourceLoader.CacheChangeType;
import ru.jengine.utils.CollectionUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Bean
public class JsonLoaderImpl implements JsonLoader {
    private final ResourceLoader resourceLoader;
    private final JsonParser jsonParser;

    private final Map<ResourceMetadata, JsonElement> jsonCache = new ConcurrentHashMap<>();

    public JsonLoaderImpl(ResourceLoader resourceLoader, JsonParser jsonParser) {
        this.resourceLoader = resourceLoader;
        this.jsonParser = jsonParser;

        resourceLoader.addCacheListener(((metadata, changeType) -> {
            if (CacheChangeType.REPLACE.equals(changeType) || CacheChangeType.DELETE.equals(changeType)) {
                jsonCache.remove(metadata);
            }
        }));
    }

    @Override
    public JsonElement getJson(ResourceMetadata metadata) throws JsonLoaderException, ResourceLoadingException {
        JsonElement cached = jsonCache.get(metadata);
        if (cached != null) {
            return cached;
        }

        ResourceMetadata currentMetadata = metadata;
        List<String> pathToJson = metadata.getPath();
        Deque<String> pathInnerJson = new ArrayDeque<>(pathToJson.size());

        while (!pathToJson.isEmpty() && !resourceLoader.hasResourceByMetadata(currentMetadata)) {
            String last = CollectionUtils.popLast(pathToJson);
            pathInnerJson.addFirst(last);
            currentMetadata = new ResourceMetadata(metadata.getNamespace(), metadata.getObjectType(), pathToJson);
        }

        if (pathToJson.isEmpty()) {
            throw new JsonLoaderException("Resource by metadata [%s] is not found".formatted(metadata));
        }

        String json = resourceLoader.getResource(currentMetadata);
        JsonElement parsedJson = jsonParser.parseJson(json);

        while (!pathInnerJson.isEmpty() && parsedJson.isJsonObject()) {
            String pathPart = pathInnerJson.removeFirst();
            parsedJson = parsedJson.getAsJsonObject().get(pathPart);

            if (parsedJson == null) {
                throw new JsonLoaderException("Can not found subjson by field [%s] for [%s] in [%s]"
                        .formatted(pathPart, metadata, parsedJson));
            }
        }

        if (!pathInnerJson.isEmpty()) {
            throw new JsonLoaderException("Can not resolve subjson by path [%s] for [%s] in [%s]"
                    .formatted(String.join("/", pathInnerJson), metadata, parsedJson));
        }

        return parsedJson;
    }

    @Override
    public JsonElement addResourceToCache(ResourceMetadata metadata, JsonElement resource) {
        return jsonCache.put(metadata, resource);
    }

    @Override
    public boolean hasInCache(ResourceMetadata metadata) {
        return jsonCache.containsKey(metadata);
    }

    @Override
    public void cleanCache() {
        jsonCache.clear();
    }
}
