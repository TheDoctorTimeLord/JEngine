package ru.jengine.jsonconverter.formatting;

import ru.jengine.jsonconverter.linking.JsonLinker;
import ru.jengine.jsonconverter.resources.JsonLoader;

public interface FormatterContextBuilder<C extends FormatterContext> {
    C getContext(JsonLoader jsonLoader, JsonLinker jsonLinker, JsonFormatterManager jsonFormatterManager);
}
