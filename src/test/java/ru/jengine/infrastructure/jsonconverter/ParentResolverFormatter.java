package ru.jengine.infrastructure.jsonconverter;

import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Shared;
import ru.jengine.jsonconverter.formatting.FormatterContext;
import ru.jengine.jsonconverter.formatting.JsonFormatter;
import ru.jengine.jsonconverter.standardtools.OverridingObjectManager;

import java.util.Set;

@Shared
public class ParentResolverFormatter implements JsonFormatter<FormatterContext> {
    private final OverridingObjectManager overridingObjectManager;

    public ParentResolverFormatter(OverridingObjectManager overridingObjectManager) {
        this.overridingObjectManager = overridingObjectManager;
    }

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
        JsonObject parent = context.parent(json, "parent");
        overridingObjectManager.override(json, parent);
        return true;
    }
}
