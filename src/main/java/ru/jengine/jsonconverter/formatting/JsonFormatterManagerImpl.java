package ru.jengine.jsonconverter.formatting;

import java.util.Comparator;
import java.util.List;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.linking.JsonLinker;
import ru.jengine.jsonconverter.resources.JsonLoader;
import ru.jengine.utils.serviceclasses.HasPriority;
import ru.jengine.utils.algorithms.ElementByFeaturesFinder;

import com.google.gson.JsonObject;

@Bean
public class JsonFormatterManagerImpl implements JsonFormatterManager {
    private final JsonLoader jsonLoader;
    private final JsonLinker jsonLinker;

    private FormatterContext formatterContext;

    private final ElementByFeaturesFinder<JsonObject, String, JsonFormatter> formatters =
            new ElementByFeaturesFinder<>(
                    JsonFormatter::getRequiredFields,
                    JsonObject::keySet,
                    Comparator.comparingInt(HasPriority::getPriority)
            );

    public JsonFormatterManagerImpl(JsonLoader jsonLoader, JsonLinker jsonLinker, List<JsonFormatter> formatters) {
        this.jsonLoader = jsonLoader;
        this.jsonLinker = jsonLinker;
        this.formatterContext = new FormatterContext(jsonLoader, jsonLinker, this);

        for (JsonFormatter formatter : formatters) {
            this.formatters.addElement(formatter);
        }
    }

    public void prepareContexts(JsonFormatterContextBuilder formatterContextBuilder)
    {
        this.formatterContext = formatterContextBuilder.build(jsonLoader, jsonLinker, this);
    }

    @Override
    public boolean formatJson(JsonObject json) {
        boolean canBeCached = true;

        for (JsonFormatter formatter : formatters.findAvailableElements(json)) {
            canBeCached = formatter.formatJson(json, formatterContext) && canBeCached;
        }

        return canBeCached;
    }

    @FunctionalInterface
    public interface JsonFormatterContextBuilder {
        FormatterContext build(JsonLoader jsonLoader, JsonLinker jsonLinker, JsonFormatterManager jsonFormatterManager);
    }
}
