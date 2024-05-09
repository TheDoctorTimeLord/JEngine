package ru.jengine.jsonconverter.standardtools.overriding;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Order;

import java.util.Map.Entry;

@Bean
@Order(100)
public class AddAdditionalValueStrategy extends PrefixOverridingStrategy {
    public AddAdditionalValueStrategy() {
        super("+");
    }

    @Override
    protected boolean arrayValue(JsonObject target, String field, JsonArray targetValue, JsonArray overridingValue) {
        overridingValue.addAll(targetValue);
        target.add(field, overridingValue);
        return true;
    }

    @Override
    protected boolean objectValue(JsonObject target, String field, JsonObject targetValue, JsonObject overridingValue) {
        for (Entry<String, JsonElement> fieldPair : targetValue.entrySet()) {
            overridingValue.add(fieldPair.getKey(), fieldPair.getValue());
        }
        target.add(field, overridingValue);
        return true;
    }
}
