package ru.jengine.jsonconverter.formatting;

import ru.jengine.jsonconverter.linking.JsonLinker;
import ru.jengine.jsonconverter.resources.JsonLoader;

public class SingletonFormatterContextBuilder extends AbstractSingletonFormatterContextBuilder<FormatterContext> {
    @Override
    protected FormatterContext createFormatterContextInstance(JsonLoader jsonLoader, JsonLinker jsonLinker, JsonFormatterManager jsonFormatterManager) {
        return new FormatterContext(jsonLoader, jsonLinker, jsonFormatterManager);
    }
}
