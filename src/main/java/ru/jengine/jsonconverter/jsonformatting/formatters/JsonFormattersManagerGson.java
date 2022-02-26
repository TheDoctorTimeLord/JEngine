package ru.jengine.jsonconverter.jsonformatting.formatters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants;
import ru.jengine.utils.CollectionUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Bean
public class JsonFormattersManagerGson implements JsonFormattersManager<JsonObject> {
    private final Map<String, List<JsonFormatter<JsonObject>>> formattersByType = new HashMap<>();
    private final List<JsonFormatter<JsonObject>> commonFormatters = new ArrayList<>();

    public JsonFormattersManagerGson(List<JsonFormatter<JsonObject>> jsonFormatters) {
        for (JsonFormatter<JsonObject> formatter : jsonFormatters) {
            List<String> objectTypes = formatter.getHandledTypes();
            if (objectTypes == null || objectTypes.isEmpty()) {
                commonFormatters.add(formatter);
            } else {
                objectTypes.forEach(type ->
                        formattersByType.computeIfAbsent(type, t -> new ArrayList<>()).add(formatter)
                );
            }
        }
    }

    @Override
    public JsonObject correct(JsonObject jsonObject) {
        for (JsonFormatter<JsonObject> formatter : getAvailableFormatters(jsonObject)) {
            jsonObject = formatter.correct(jsonObject);
        }
        return jsonObject;
    }

    @Override
    public List<String> extractJsonDependencies(JsonObject jsonObject) {
        Set<String> result = new HashSet<>();

        for (JsonFormatter<JsonObject> formatter : getAvailableFormatters(jsonObject)) {
            result.addAll(formatter.extractJsonDependencies(jsonObject));
        }

        return new ArrayList<>(result);
    }

    @Override
    public boolean formatting(JsonObject mainJson, Map<String, JsonObject> dependencies) {
        boolean needPostHandling = false;
        for (JsonFormatter<JsonObject> formatter : getAvailableFormatters(mainJson)) {
            needPostHandling = formatter.formatting(mainJson, dependencies) || needPostHandling;
        }
        return needPostHandling;
    }

    @Override
    public void cleanResultObject(JsonObject resultJson) {
        for (JsonFormatter<JsonObject> formatter : getAvailableFormatters(resultJson)) {
            formatter.cleanResultObject(resultJson);
        }
    }

    private List<JsonFormatter<JsonObject>> getAvailableFormatters(JsonObject jsonObject) {
        String objectType = extractObjectType(jsonObject);
        List<JsonFormatter<JsonObject>> formatters = formattersByType.get(objectType);
        return formatters == null ? commonFormatters : CollectionUtils.concat(formatters, commonFormatters);
    }

    private static String extractObjectType(JsonObject jsonObject) {
        JsonElement element = jsonObject.get(Constants.JsonFormatter.OBJECT_TYPE_NAME);
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsString();
        }
        return Constants.JsonFormatter.UNKNOWN_OBJECT_TYPE;
    }
}
