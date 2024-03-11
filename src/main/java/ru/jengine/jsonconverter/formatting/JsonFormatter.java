package ru.jengine.jsonconverter.formatting;

import com.google.gson.JsonObject;
import ru.jengine.utils.serviceclasses.HasPriority;

import java.util.Set;

public interface JsonFormatter<C extends FormatterContext> extends HasPriority {
    Set<String> getRequiredFields();
    boolean formatJson(JsonObject json, C context);
}
