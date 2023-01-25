package ru.jengine.jsonconverter.linking;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.exceptions.JsonConverterException;
import ru.jengine.jsonconverter.formatting.FormatterContext;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Bean
public class JsonLinker {
    private final LinkExtractorManager linkExtractorManager;

    public JsonLinker(LinkExtractorManager linkExtractorManager) {
        this.linkExtractorManager = linkExtractorManager;
    }

    public ResourceMetadata extractMetadataFormField(JsonObject json, String linkProperty) {
        String rawLink = FormatterContext.asString(json, linkProperty);
        ResourceMetadata link = linkExtractorManager.extractLink(rawLink);
        if (link == null) {
            throw new JsonConverterException("Property [%s] has incorrect link in json [%s]"
                    .formatted(linkProperty, json));
        }

        return link;
    }

    public void link(JsonObject json, String field, JsonElement linkedJsonElement) {
        json.add(field, linkedJsonElement);
    }

    public LinkExtractorManager getLinkExtractorManager() {
        return linkExtractorManager;
    }
}
