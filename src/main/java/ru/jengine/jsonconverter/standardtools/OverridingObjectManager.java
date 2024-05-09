package ru.jengine.jsonconverter.standardtools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.standardtools.overriding.FieldOverridingStrategy;

import java.util.List;
import java.util.Map.Entry;

@Bean
public class OverridingObjectManager {
    private final List<FieldOverridingStrategy> strategies;

    public OverridingObjectManager(List<FieldOverridingStrategy> strategies) {
        this.strategies = strategies;
    }

    public void override(JsonObject overridingObject, JsonObject parentObject) {
        for (Entry<String, JsonElement> field : parentObject.getAsJsonObject().entrySet()) {
            String parentField = field.getKey();
            JsonElement parentFieldValue = field.getValue().deepCopy();

            for (FieldOverridingStrategy strategy : strategies) {
                if (strategy.override(overridingObject, parentField, parentFieldValue)) {
                    break;
                }
            }
        }
    }
}
