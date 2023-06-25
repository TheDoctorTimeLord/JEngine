package ru.jengine.jsonconverter.additional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import ru.jengine.jsonconverter.formatting.FormatterContext;
import ru.jengine.jsonconverter.formatting.JsonFormatter;

import com.google.gson.JsonObject;

public class CustomFormatter implements JsonFormatter {
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
