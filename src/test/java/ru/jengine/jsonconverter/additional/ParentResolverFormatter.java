package ru.jengine.jsonconverter.additional;

import java.util.Set;

import ru.jengine.jsonconverter.formatting.FormatterContext;
import ru.jengine.jsonconverter.formatting.JsonFormatter;

import com.google.gson.JsonObject;

public class ParentResolverFormatter implements JsonFormatter {
    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Set<String> getRequiredFields() {
        return Set.of("parent");
    }

    @Override
    public boolean formatJson(JsonObject json, FormatterContext context) {
        context.parent(json, "parent");
        return true;
    }
}
