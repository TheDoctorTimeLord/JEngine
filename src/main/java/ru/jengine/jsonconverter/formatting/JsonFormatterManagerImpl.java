package ru.jengine.jsonconverter.formatting;

import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.SharedBeansProvider;
import ru.jengine.jsonconverter.linking.JsonLinker;
import ru.jengine.jsonconverter.resources.JsonLoader;
import ru.jengine.utils.algorithms.ElementByFeaturesFinder;
import ru.jengine.utils.serviceclasses.HasPriority;

import java.util.Comparator;
import java.util.List;

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
