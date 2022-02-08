package ru.jengine.jsonconverter;

public class JsonContext {
    private final String convertedJson;

    public JsonContext(String convertedJson) {
        this.convertedJson = convertedJson;
    }

    public String getConvertedJson() {
        return convertedJson;
    }
}
