package ru.jengine.jsonconverter.standardtools;

import java.util.List;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.JsonContext;
import ru.jengine.jsonconverter.converting.ConverterToObject;
import ru.jengine.jsonconverter.exceptions.JsonConverterRuntimeException;
import ru.jengine.utils.ReflectionUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

@Bean
public class ConverterToObjectGson implements ConverterToObject {
    private final Gson gson;

    public ConverterToObjectGson(List<JsonConverterDeserializer<?>> deserializers) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        for (JsonConverterDeserializer<?> deserializer : deserializers) {
            Class<?> type = ReflectionUtils.getGenericHierarchyInterfaceType(deserializer.getClass(), JsonConverterDeserializer.class, 0);

            if (deserializer.inHierarchy()) {
                gsonBuilder.registerTypeHierarchyAdapter(type, deserializer);
            } else {
                gsonBuilder.registerTypeAdapter(type, deserializer);
            }
        }

        this.gson = gsonBuilder.create();
    }

    @Override
    public <T> T convertToObject(JsonContext jsonContext, Class<T> convertedResultClass) {
        try {
            return gson.fromJson(jsonContext.getConvertedJson(), convertedResultClass);
        } catch (JsonSyntaxException e) {
            throw new JsonConverterRuntimeException(String.format("Json [%s] can not be converted to [%s]",
                    jsonContext.getConvertedJson(), convertedResultClass), e);
        }
    }
}
