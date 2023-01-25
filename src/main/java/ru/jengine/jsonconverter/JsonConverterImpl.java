package ru.jengine.jsonconverter;

import java.util.List;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.exceptions.JsonConverterException;
import ru.jengine.jsonconverter.exceptions.JsonLoaderException;
import ru.jengine.jsonconverter.exceptions.ResourceLoadingException;
import ru.jengine.jsonconverter.formatting.JsonFormatterManager;
import ru.jengine.jsonconverter.resources.JsonLoader;
import ru.jengine.jsonconverter.resources.JsonParser;
import ru.jengine.jsonconverter.resources.ResourceMetadata;
import ru.jengine.jsonconverter.serializeprocess.JsonConverterDeserializer;
import ru.jengine.jsonconverter.serializeprocess.JsonConverterSerializer;
import ru.jengine.utils.ReflectionUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

@Bean
public class JsonConverterImpl implements JsonConverter {
    private final Gson gson;
    private final JsonParser jsonParser;
    private final JsonLoader jsonLoader;
    private final JsonFormatterManager jsonFormatterManager;

    public JsonConverterImpl(JsonLoader jsonLoader, JsonFormatterManager jsonFormatterManager, JsonParser jsonParser,
            @Nullable GsonBuilderConfigurer gsonBuilderConfigurer, List<JsonConverterSerializer<?>> serializers,
            List<JsonConverterDeserializer<?>> deserializers)
    {
        this.jsonLoader = jsonLoader;
        this.jsonFormatterManager = jsonFormatterManager;
        this.jsonParser = jsonParser;
        this.gson = prepareGson(
                gsonBuilderConfigurer != null ? gsonBuilderConfigurer : gsonBuilder -> {},
                serializers,
                deserializers
        );
    }

    private static Gson prepareGson(GsonBuilderConfigurer configurer, List<JsonConverterSerializer<?>> serializers,
            List<JsonConverterDeserializer<?>> deserializers)
    {
        GsonBuilder builder = new GsonBuilder();

        addSerializers(builder, serializers);
        addDeserializers(builder, deserializers);
        configurer.configure(builder);

        return builder.create();
    }

    private static void addSerializers(GsonBuilder builder, List<JsonConverterSerializer<?>> serializers) {
        for (JsonConverterSerializer<?> serializer : serializers) {
            Class<?> type = ReflectionUtils.getGenericHierarchyInterfaceType(serializer.getClass(), JsonConverterDeserializer.class, 0);

            if (serializer.inHierarchy()) {
                builder.registerTypeHierarchyAdapter(type, serializer);
            } else {
                builder.registerTypeAdapter(type, serializer);
            }
        }
    }

    private static void addDeserializers(GsonBuilder builder, List<JsonConverterDeserializer<?>> deserializers) {
        for (JsonConverterDeserializer<?> deserializer : deserializers) {
            Class<?> type = ReflectionUtils.getGenericHierarchyInterfaceType(deserializer.getClass(), JsonConverterDeserializer.class, 0);

            if (deserializer.inHierarchy()) {
                builder.registerTypeHierarchyAdapter(type, deserializer);
            } else {
                builder.registerTypeAdapter(type, deserializer);
            }
        }
    }

    @Override
    public JsonElement convertToJson(Object converingObject) {
        return gson.toJsonTree(converingObject);
    }

    @Override
    public <T> T convertFromJson(ResourceMetadata resource, Class<T> convertTo) {
        try {
            return convertFromJson(jsonLoader.getJson(resource), convertTo, jsonLoader.hasInCache(resource));
        }
        catch (JsonLoaderException | ResourceLoadingException e) {
            throw new JsonConverterException("Resource can not be loaded [%s]".formatted(resource), e);
        }
    }

    @Override
    public <T> T convertFromJson(String json, Class<T> convertTo) {
        return convertFromJson(jsonParser.parseJson(json), convertTo, true);
    }

    private <T> T convertFromJson(JsonElement jsonElement, Class<T> convertTo, boolean needFormat) {
        if (needFormat) {
            jsonFormatterManager.formatJson(jsonElement);
        }

        try {
            return gson.fromJson(jsonElement, convertTo);
        }
        catch (JsonSyntaxException e) {
            throw new JsonConverterException("Json can not be converted. From [%s] to [%s]"
                    .formatted(jsonElement, convertTo), e);
        }
    }
}
