package ru.jengine.jsonconverter.standardtools.overriding;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Order;

@Bean
@Order(600)
public class NotOverridingFieldStrategy implements FieldOverridingStrategy {
    @Override
    public boolean override(JsonObject targetObject, String fieldName, JsonElement overridingValue) {
        if (!targetObject.has(fieldName)) {
            targetObject.add(fieldName, overridingValue);
            return true;
        }
        return false;
    }
}
