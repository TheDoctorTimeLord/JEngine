package ru.jengine.jsonconverter.linking;

import javax.annotation.Nullable;

import ru.jengine.jsonconverter.resources.ResourceMetadata;

public interface LinkExtractor {
    @Nullable
    ResourceMetadata extractLink(String rawLink);
}
