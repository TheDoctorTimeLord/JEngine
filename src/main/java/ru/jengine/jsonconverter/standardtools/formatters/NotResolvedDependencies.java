package ru.jengine.jsonconverter.standardtools.formatters;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.jsonformatting.formatters.BaseGsonFormatter;
import ru.jengine.jsonconverter.utils.JsonUtils;

import com.google.gson.JsonObject;

@Bean
public class NotResolvedDependencies extends BaseGsonFormatter {
    @Override
    public List<String> getHandledTypes() {
        return Collections.emptyList();
    }

    @Override
    public void cleanResultObject(JsonObject resultObject) {
        List<String> filedNames = resultObject.entrySet().stream()
                .filter(entry -> JsonUtils.isLink(entry.getValue()))
                .map(Entry::getKey)
                .collect(Collectors.toList());

        for (String fieldName : filedNames) {
            resultObject.add(fieldName, null);
        }
    }
}
