package ru.jengine.jsonconverter.jsonformatting;

import ru.jengine.jsonconverter.JsonContext;
import ru.jengine.jsonconverter.exceptions.JsonConverterRuntimeException;
import ru.jengine.jsonconverter.resources.ResourceLoader;

public interface JsonFormatterModule {
    JsonContext formatJson(String json, ResourceLoader loader, boolean inlineDependencies) throws JsonConverterRuntimeException;
}
