package ru.jengine.jsonconverter.jsonformatting.linking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.exceptions.JsonConvertException;
import ru.jengine.jsonconverter.resources.ResourceLoader;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Bean
public class JsonLinkerGson implements JsonLinker<JsonObject> {
    @Override
    public Map<String, String> getDependedJsons(JsonObject json, List<String> fieldsDependencies, ResourceLoader resourceLoader)
            throws JsonConvertException
    {
        Map<String, String> dependencies = new HashMap<>(fieldsDependencies.size());

        for (String field : fieldsDependencies) {
            JsonElement rawValue = json.get(field);
            if (rawValue == null || !rawValue.isJsonPrimitive()) {
                throw new JsonConvertException(String.format("Value [%s] of field [%s] is not string on json [%s]",
                        rawValue, field, json));
            }
            String linkToJson = rawValue.getAsString();

            try {
                ResourceMetadata parsedMetadata = extractMetadata(linkToJson);
                dependencies.put(linkToJson, resourceLoader.getResource(parsedMetadata));
            }
            catch (Exception e) {
                throw new JsonConvertException(String.format("Resource by value [%s] of field [%s] in json [%s] not found",
                        linkToJson, field, json), e);
            }
        }

        return dependencies;
    }

    private static ResourceMetadata extractMetadata(String linkToJson) throws JsonConvertException {
        int namespaceDelimiter = linkToJson.indexOf(":");
        if (namespaceDelimiter == -1) {
            throw new JsonConvertException("Link [%s] don't have namespace");
        }
        String namespace = linkToJson.substring(0, namespaceDelimiter);
        linkToJson = linkToJson.substring(namespaceDelimiter + 1);

        int objectTypeDelimiter = linkToJson.indexOf(":");
        String objectType = null;
        if (objectTypeDelimiter != -1) {
            objectType = linkToJson.substring(0, objectTypeDelimiter);
            linkToJson = linkToJson.substring(objectTypeDelimiter + 1);
        }

        return new ResourceMetadata(namespace, objectType, linkToJson);
    }
}
