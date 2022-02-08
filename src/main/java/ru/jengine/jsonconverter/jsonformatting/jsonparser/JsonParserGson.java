package ru.jengine.jsonconverter.jsonformatting.jsonparser;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.exceptions.JsonConverterRuntimeException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@Bean
public class JsonParserGson implements JsonParser<JsonObject> {
    @Override
    public JsonObject parse(String json) {
        try {
            JsonElement jsonElement = com.google.gson.JsonParser.parseString(json);
            if (!jsonElement.isJsonObject()) {
                throw new JsonSyntaxException("Json is not object");
            }
            return (JsonObject)jsonElement;
        } catch (JsonSyntaxException ex) {
            throw new JsonConverterRuntimeException(String.format("Error when read [%s]", json), ex);
        }
    }
}
