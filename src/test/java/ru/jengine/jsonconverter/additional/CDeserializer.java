package ru.jengine.jsonconverter.additional;

import java.lang.reflect.Type;


import ru.jengine.jsonconverter.serializeprocess.JsonConverterDeserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CDeserializer implements JsonConverterDeserializer<C> {
    @Override
    public C deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            return context.deserialize(json, D.class);
        }
        return null;
    }
}
