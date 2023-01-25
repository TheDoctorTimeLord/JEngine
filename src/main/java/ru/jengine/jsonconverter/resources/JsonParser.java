package ru.jengine.jsonconverter.resources;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.exceptions.JsonConverterException;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

@Bean
public class JsonParser {
    public JsonElement parseJson(String json) throws JsonConverterException {
        try {
            return com.google.gson.JsonParser.parseString(json);
        }
        catch (JsonSyntaxException e) {
            throw new JsonConverterException("Resource has incorrect format [%s]".formatted(json), e);
        }
    }
}
