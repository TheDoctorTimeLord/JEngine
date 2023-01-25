package ru.jengine.jsonconverter.formatting;

import java.util.Set;

import ru.jengine.utils.HasPriority;

import com.google.gson.JsonObject;

public interface JsonFormatter extends HasPriority {
    Set<String> getRequiredFields();
    boolean formatJson(JsonObject json, FormatterContext context);
}
