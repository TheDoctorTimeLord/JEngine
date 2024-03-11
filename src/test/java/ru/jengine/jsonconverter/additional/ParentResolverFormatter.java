package ru.jengine.jsonconverter.additional;

import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Shared;
import ru.jengine.jsonconverter.formatting.FormatterContext;
import ru.jengine.jsonconverter.formatting.JsonFormatter;

import java.util.Set;

@Shared
public class ParentResolverFormatter implements JsonFormatter<FormatterContext> {
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
