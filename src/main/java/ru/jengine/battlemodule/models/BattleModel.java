package ru.jengine.battlemodule.models;

import java.util.HashMap;
import java.util.Map;

public abstract class BattleModel {
    private final int id;
    private final Map<String, Object> properties = new HashMap<>();

    protected BattleModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    protected <T> T getProperty(String propertyCode) {
        return (T) properties.get(propertyCode);
    }

    protected void setProperty(String propertyCode, Object propertyValue) {
        properties.put(propertyCode, propertyValue);
    }
}
