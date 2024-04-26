package ru.jengine.jsonconverter.linking;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.SharedBeansProvider;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

import javax.annotation.Nullable;
import java.util.List;

@Bean
public class LinkExtractorManager {
    private List<LinkExtractor> extractors = List.of();

    @SharedBeansProvider
    private void provideLinkExtractors(List<LinkExtractor> extractors) {
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
