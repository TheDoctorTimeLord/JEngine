package ru.jengine.jsonconverter.standardtools.overriding;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Order;

@Bean
@Order(200)
public class RemoveOverridingValueStrategy extends PrefixOverridingStrategy {
    public RemoveOverridingValueStrategy() {
        super("-");
    }

    @Override
    protected boolean arrayValue(JsonObject target, String field, JsonArray targetValue, JsonArray overridingValue) {
        for (JsonElement element : targetValue) {
            overridingValue.remove(element);
        }
        target.add(field, overridingValue);
        return true;
    }

    @Override
    protected boolean objectValue(JsonObject target, String field, JsonObject targetValue, JsonObject overridingValue) {
        for (String fieldName : targetValue.keySet()) {
            overridingValue.remove(fieldName);
        }
        target.add(field, overridingValue);
        return true;
    }
}
