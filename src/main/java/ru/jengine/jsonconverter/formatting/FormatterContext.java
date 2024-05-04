package ru.jengine.jsonconverter.formatting;

import com.google.gson.*;
import ru.jengine.jsonconverter.JsonConverterConstants;
import ru.jengine.jsonconverter.exceptions.JsonConverterException;
import ru.jengine.jsonconverter.exceptions.JsonLoaderException;
import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;
import ru.jengine.jsonconverter.linking.JsonLinker;
import ru.jengine.jsonconverter.linking.LinkExtractorManager;
import ru.jengine.jsonconverter.resources.JsonLoader;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

import java.util.List;
import java.util.Map.Entry;

public class FormatterContext {
    private final JsonLoader jsonLoader;
    private final JsonLinker jsonLinker;
    private final JsonFormatterManager jsonFormatterManager;

    public FormatterContext(JsonLoader jsonLoader, JsonLinker jsonLinker, JsonFormatterManager jsonFormatterManager) {
        this.jsonLoader = jsonLoader;
        this.jsonLinker = jsonLinker;
        this.jsonFormatterManager = jsonFormatterManager;
    }

    public JsonElement link(JsonObject json, String field) {
        ResourceMetadata metadata = jsonLinker.extractMetadataFormField(json, field);
        JsonElement linkedJson;
        try {
            linkedJson = jsonLoader.getJson(metadata);
            if (linkedJson.isJsonNull()) {
                throw new JsonConverterException("Linked json was null");
            }
        }
        catch (JsonConverterException | JsonLoaderException | ResourceLoadingException e) {
            throw new JsonConverterException("Can not load subjson by field [%s] in [%s]".formatted(field, json), e);
        }

        boolean canBeCached = false;
        if (!jsonLoader.hasInCache(metadata)) {
            canBeCached = jsonFormatterManager.formatJson(linkedJson);
        }

        if (canBeCached) {
            jsonLoader.addResourceToCache(metadata, linkedJson);
        }

        jsonLinker.link(json, field, linkedJson.deepCopy());

        return linkedJson;
    }

    public void parent(JsonObject json, String field) {
        JsonElement linkedJson = link(json, field);
        if (!linkedJson.isJsonObject()) {
            throw new JsonConverterException("Resolved parent is not object. Parent: [%s], field [%s] in json [%s]"
                    .formatted(linkedJson, field, json));
        }

        json.remove(field);

        for (Entry<String, JsonElement> entry : linkedJson.getAsJsonObject().entrySet()) {
            String parentField = entry.getKey();
            JsonElement jsonElement = entry.getValue();

            if (!json.has(parentField)) {
                json.add(parentField, jsonElement.deepCopy());
            }
        }
    }

    public List<String> fieldsWithLink(JsonObject json) {
        LinkExtractorManager linkExtractorManager = jsonLinker.getLinkExtractorManager();
        return json.entrySet().stream()
                .filter(entry -> {
                    JsonElement value = entry.getValue();
                    if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
                        return false;
                    }
                    return linkExtractorManager.extractLink(value.getAsString()) != null;
                })
                .map(Entry::getKey)
                .toList();
    }

    public void ifType(JsonObject json, String expectedType, Runnable handler) {
        String type = asString(json, JsonConverterConstants.TYPE);
        if (expectedType.equals(type)) {
            json.remove(JsonConverterConstants.TYPE);
            handler.run();
        }
    }

    public void format(JsonElement json) {
        jsonFormatterManager.formatJson(json);
    }

    public static JsonPrimitive asPrimitive(JsonObject json, String field) {
        return asPrimitive(json, field, false);
    }

    public static JsonPrimitive asPrimitive(JsonObject json, String field, boolean remove) {
        JsonElement element = remove ? json.remove(field) : json.get(field);
        if (element == null || !element.isJsonPrimitive()) {
            throw new JsonConverterException("Field [%s] must be primitive in [%s]".formatted(field, json));
        }
        return element.getAsJsonPrimitive();
    }

    public static String asString(JsonObject json, String field) {
        return asString(json, field, false);
    }

    public static String asString(JsonObject json, String field, boolean remove) {
        JsonPrimitive primitive = asPrimitive(json, field, remove);
        if (!primitive.isString()) {
            throw new JsonConverterException("Field [%s] must be string in [%s]".formatted(field, json));
        }
        return primitive.getAsString();
    }

    public static JsonArray asJsonArray(JsonObject json, String field) {
        JsonElement value = json.get(field);
        if (value == null || !value.isJsonArray()) {
            throw new JsonConverterException("Field [%s] must be array in [%s]".formatted(field, json));
        }
        return value.getAsJsonArray();
    }

    public static JsonObject asJsonObject(JsonObject json, String field, String... innerExistingFields) {
        JsonElement value = json.get(field);
        if (value == null || !value.isJsonObject()) {
            throw new JsonConverterException("Field [%s] must be json in [%s]".formatted(field, json));
        }

        JsonObject extractedObject = value.getAsJsonObject();
        for (String innerExistingField : innerExistingFields) {
            if (!extractedObject.has(innerExistingField)) {
                throw new JsonParseException("Json doesn't have field [%s] in [%s]".formatted(field, json));
            }
        }
        return extractedObject;
    }

    public static Class<?> classPath(JsonObject json) throws JsonParseException {
        JsonPrimitive classPathPrimitive = asPrimitive(json, JsonConverterConstants.CLASS_PATH_FIELD, true);
        if (!classPathPrimitive.isString()) {
            throw new JsonConverterException("Class path is incorrect. ClassPath [%s] in [%s]"
                    .formatted(classPathPrimitive, json));
        }

        String classPathStr = classPathPrimitive.getAsString();

        try {
            return FormatterContext.class.getClassLoader().loadClass(classPathStr);
        }
        catch (ClassNotFoundException e) {
            throw new JsonConverterException("Class path is incorrect [%s]".formatted(classPathStr), e);
        }
    }

    public static void setClassPath(JsonObject json, Class<?> cls) {
        json.addProperty(JsonConverterConstants.CLASS_PATH_FIELD, cls.getName());
    }
}
