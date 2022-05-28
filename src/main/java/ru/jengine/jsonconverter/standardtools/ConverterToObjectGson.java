package ru.jengine.jsonconverter.standardtools;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.JsonContext;
import ru.jengine.jsonconverter.converting.ConverterToObject;
import ru.jengine.jsonconverter.exceptions.JsonConverterRuntimeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

@Bean
public class ConverterToObjectGson implements ConverterToObject {
    private enum GSON {
        ; public static final Gson INSTANCE = new GsonBuilder().create();
    }

    @Override
    public <T> T convertToObject(JsonContext jsonContext, Class<T> convertedResultClass) {
        try {
            return GSON.INSTANCE.fromJson(jsonContext.getConvertedJson(), convertedResultClass);
        } catch (JsonSyntaxException e) {
            throw new JsonConverterRuntimeException(String.format("Json [%s] can not be converted to [%s]",
                    jsonContext.getConvertedJson(), convertedResultClass), e);
        }
    }
}
