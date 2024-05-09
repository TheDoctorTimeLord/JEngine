package ru.jengine.infrastructure.jsonconverter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import ru.jengine.beancontainer.annotations.Shared;
import ru.jengine.jsonconverter.serializeprocess.JsonConverterDeserializer;

import java.lang.reflect.Type;

@Shared
public class CDeserializer implements JsonConverterDeserializer<C> {
    @Override
    public C deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            return context.deserialize(json, D.class);
        }
        return null;
    }
}
