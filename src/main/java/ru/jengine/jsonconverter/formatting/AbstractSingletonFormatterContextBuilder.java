package ru.jengine.jsonconverter.formatting;

import ru.jengine.jsonconverter.linking.JsonLinker;
import ru.jengine.jsonconverter.resources.JsonLoader;

public abstract class AbstractSingletonFormatterContextBuilder<C extends FormatterContext> implements FormatterContextBuilder<C> {
    private volatile C formatterContext;

    private C getInstance(JsonLoader jsonLoader, JsonLinker jsonLinker, JsonFormatterManager jsonFormatterManager) {
        if (formatterContext == null) {
            synchronized (this) {
                if (formatterContext == null) {
                    formatterContext = createFormatterContextInstance(jsonLoader, jsonLinker, jsonFormatterManager);
                }
            }
        }
        return formatterContext;
    }

    protected abstract C createFormatterContextInstance(JsonLoader jsonLoader, JsonLinker jsonLinker,
            JsonFormatterManager jsonFormatterManager);

    @Override
    public C getContext(JsonLoader jsonLoader, JsonLinker jsonLinker, JsonFormatterManager jsonFormatterManager) {
        return getInstance(jsonLoader, jsonLinker, jsonFormatterManager);
    }
}
