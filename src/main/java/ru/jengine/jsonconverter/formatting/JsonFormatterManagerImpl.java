package ru.jengine.jsonconverter.formatting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.SharedBeansProvider;
import ru.jengine.jsonconverter.linking.JsonLinker;
import ru.jengine.jsonconverter.resources.JsonLoader;
import ru.jengine.utils.algorithms.ElementByFeaturesFinder;
import ru.jengine.utils.serviceclasses.HasPriority;

import java.util.*;
import java.util.Map.Entry;

@Bean
public class JsonFormatterManagerImpl implements JsonFormatterManager {
    private final JsonLoader jsonLoader;
    private final JsonLinker jsonLinker;

    private FormatterContext formatterContext;

    private final ElementByFeaturesFinder<JsonObject, String, JsonFormatter<?>> formatters =
            new ElementByFeaturesFinder<>(
                    JsonFormatter::getRequiredFields,
                    JsonObject::keySet,
                    Comparator.comparingInt(HasPriority::getPriority)
            );

    public JsonFormatterManagerImpl(JsonLoader jsonLoader, JsonLinker jsonLinker) {
        this.jsonLoader = jsonLoader;
        this.jsonLinker = jsonLinker;
        this.formatterContext = new FormatterContext(jsonLoader, jsonLinker, this);
    }

    @SharedBeansProvider
    private void provideFormatters(List<JsonFormatter<?>> formatters) {
        this.formatters.clear();
        for (JsonFormatter<?> formatter : formatters) {
            this.formatters.addElement(formatter);
        }
    }

    public void registerFormatter(JsonFormatter<?> formatter) {
        this.formatters.addElement(formatter);
    }

    public void removeFormatter(JsonFormatter<?> formatter) {
        this.formatters.removeElement(formatter);
    }

    public void prepareContexts(JsonFormatterContextBuilder formatterContextBuilder)
    {
        this.formatterContext = formatterContextBuilder.build(jsonLoader, jsonLinker, this);
    }

    @Override
    public boolean formatJson(JsonObject json) {
        //Проходим BFS по всем объектам дерева и собираем стек обратного прохода для dформатирования
        List<JsonObject> objectHierarchy = new ArrayList<>();
        objectHierarchy.add(json);
        int currentElement = 0;

        while (currentElement < objectHierarchy.size()) {
            JsonObject currentObject = objectHierarchy.get(currentElement++);
            for (Entry<String, JsonElement> entry : currentObject.entrySet()) {
                JsonElement fieldValue = entry.getValue();
                if (fieldValue.isJsonObject()) {
                    objectHierarchy.add(fieldValue.getAsJsonObject());
                }
                if (fieldValue.isJsonArray()) {
                    for (JsonElement jsonElement : fieldValue.getAsJsonArray()) {
                        if (jsonElement.isJsonObject()) {
                            objectHierarchy.add(jsonElement.getAsJsonObject());
                        }
                    }
                }
            }
        }

        boolean canBeCached = true;
        for (int i = objectHierarchy.size() - 1; i >= 0; i--) {
            canBeCached = useFormatters(objectHierarchy.get(i)) && canBeCached;
        }
        return canBeCached;
    }

    public boolean useFormatters(JsonObject json) {
        boolean canBeCached = true;
        for (JsonFormatter<?> formatter : formatters.findAvailableElements(json)) {
            canBeCached = formatter.formatJson(json, getContext()) && canBeCached;
        }
        return canBeCached;
    }

    @SuppressWarnings("unchecked")
    private <C extends FormatterContext> C getContext() {
        return (C)formatterContext;
    }

    @FunctionalInterface
    public interface JsonFormatterContextBuilder {
        FormatterContext build(JsonLoader jsonLoader, JsonLinker jsonLinker, JsonFormatterManager jsonFormatterManager);
    }
}
