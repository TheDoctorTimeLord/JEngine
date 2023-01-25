package ru.jengine.jsonconverter.linking;

import java.util.List;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

@Bean
public class LinkExtractorManager {
    private final List<LinkExtractor> extractors;

    public LinkExtractorManager(List<LinkExtractor> extractors) {
        this.extractors = extractors;
    }

    @Nullable
    public ResourceMetadata extractLink(String rawLink) {
        for (LinkExtractor extractor : extractors) {
            ResourceMetadata resourceMetadata = extractor.extractLink(rawLink);
            if (resourceMetadata != null) {
                return resourceMetadata;
            }
        }
        return null;
    }
}
