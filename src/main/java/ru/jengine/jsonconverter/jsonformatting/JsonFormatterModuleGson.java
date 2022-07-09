package ru.jengine.jsonconverter.jsonformatting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.JsonContext;
import ru.jengine.jsonconverter.exceptions.JsonConvertException;
import ru.jengine.jsonconverter.exceptions.JsonConverterRuntimeException;
import ru.jengine.jsonconverter.jsonformatting.formatters.JsonFormattersManager;
import ru.jengine.jsonconverter.jsonformatting.jsonparser.JsonParser;
import ru.jengine.jsonconverter.jsonformatting.linking.JsonLinker;
import ru.jengine.jsonconverter.resources.ResourceLoader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Bean
public class JsonFormatterModuleGson implements JsonFormatterModule {
    private final JsonParser<JsonObject> jsonParser;
    private final JsonFormattersManager<JsonObject> formattersManager;
    private final JsonLinker<JsonObject> jsonLinker;

    public JsonFormatterModuleGson(JsonParser<JsonObject> jsonParser,
            JsonFormattersManager<JsonObject> formattersManager, JsonLinker<JsonObject> jsonLinker) {
        this.jsonParser = jsonParser;
        this.formattersManager = formattersManager;
        this.jsonLinker = jsonLinker;
    }

    @Override
    public JsonContext formatJson(String json, ResourceLoader loader, boolean inlineDependencies)
            throws JsonConverterRuntimeException
    {
        JsonObject startedJson = parseAndCorrectJson(json);

        if (!inlineDependencies) {
            formattersManager.formatting(startedJson, new HashMap<>());
        } else {
            formattingJsonObjectWithFields(startedJson, loader, new HashMap<>());
        }

        formattersManager.cleanResultObject(startedJson);
        return new JsonContext(startedJson.toString());
    }

    private void formattingJsonObjectWithFields(JsonObject jsonObject, ResourceLoader loader,
            Map<String, JsonObject> filledJsons)
    {
        JsonObject formattedObject = formattingJsonObject(jsonObject, loader, filledJsons);

        for (Entry<String, JsonElement> attrValue : formattedObject.entrySet()) {
            JsonElement value = attrValue.getValue();
            if (value.isJsonObject()) {
                formattingJsonObjectWithFields(value.getAsJsonObject(), loader, filledJsons);
            }
        }
    }

    private JsonObject formattingJsonObject(JsonObject jsonObject, ResourceLoader loader,
            Map<String, JsonObject> filledJsons)
    {
        List<String> fieldsDependencies = formattersManager.extractJsonDependencies(jsonObject);
        Map<String, String> dependenciesJsons = linkDependencies(jsonObject, fieldsDependencies, loader);
        Map<String, JsonObject> dependenciesJsonObjects = new HashMap<>(dependenciesJsons.size());

        for (Map.Entry<String, String> entry : dependenciesJsons.entrySet()) {
            if (!filledJsons.containsKey(entry.getKey())) {
                JsonObject notFilledJson = parseAndCorrectJson(entry.getValue());
                JsonObject formattedJson = formattingJsonObject(notFilledJson, loader, filledJsons);
                filledJsons.put(entry.getKey(), formattedJson);
            }

            JsonObject filledJson = filledJsons.get(entry.getKey());
            dependenciesJsonObjects.put(entry.getKey(), filledJson);
        }

        if (formattersManager.formatting(jsonObject, dependenciesJsonObjects)) {
            jsonObject = formattersManager.correct(jsonObject);
            return formattingJsonObject(jsonObject, loader, filledJsons);
        }
        return jsonObject;
    }

    private Map<String, String> linkDependencies(JsonObject jsonObject, List<String> fieldsDependencies, ResourceLoader loader) {
        try {
            return jsonLinker.getDependedJsons(jsonObject, fieldsDependencies, loader);
        }
        catch (JsonConvertException e) {
            throw new JsonConverterRuntimeException("Error when linking dependencies", e);
        }
    }

    private JsonObject parseAndCorrectJson(String json) {
        JsonObject jsonObject = jsonParser.parse(json);
        return formattersManager.correct(jsonObject);
    }
}
