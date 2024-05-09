package ru.jengine.infrastructure.jsonconverter;

import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Shared;
import ru.jengine.jsonconverter.formatting.FormatterContext;
import ru.jengine.jsonconverter.formatting.JsonFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Shared
public class CustomFormatter implements JsonFormatter<FormatterContext> {
    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Set<String> getRequiredFields() {
        return Collections.emptySet();
    }

    @Override
    public boolean formatJson(JsonObject json, FormatterContext context) {
        List<String> linkFields = context.fieldsWithLink(json);
        for (String linkField : linkFields) {
            context.link(json, linkField);
        }
        return true;
    }
}
