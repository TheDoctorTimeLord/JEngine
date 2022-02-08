package ru.jengine.jsonconverter.jsonformatting.jsonparser;

public interface JsonParser<JO> {
    JO parse(String json);
}
