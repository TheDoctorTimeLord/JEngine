package ru.jengine.jsonconverter.standardtools.formatters;

import java.util.Collections;
import java.util.List;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants.JsonFormatter;
import ru.jengine.jsonconverter.jsonformatting.formatters.BaseGsonFormatter;

import com.google.gson.JsonObject;

@Bean
public class ObjectTypeCleaner extends BaseGsonFormatter {
    @Override
    public List<String> getHandledTypes() {
        return Collections.emptyList();
    }

    @Override
    public void cleanResultObject(JsonObject resultObject) {
        resultObject.remove(JsonFormatter.OBJECT_TYPE_NAME);
    }
}
