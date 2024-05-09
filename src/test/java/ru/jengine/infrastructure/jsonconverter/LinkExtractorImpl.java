package ru.jengine.infrastructure.jsonconverter;

import org.jetbrains.annotations.Nullable;
import ru.jengine.beancontainer.annotations.Shared;
import ru.jengine.jsonconverter.linking.LinkExtractor;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Shared
public class LinkExtractorImpl implements LinkExtractor {
    private static final Pattern LINK_PATTERN = Pattern.compile("(\\w+):(\\w+)(:(\\w+))?");

    @Nullable
    @Override
    public ResourceMetadata extractLink(String rawLink) {
        Matcher matcher = LINK_PATTERN.matcher(rawLink);
        if (matcher.matches()) {
            String objectType = matcher.group(3) != null
                    ? matcher.group(2)
                    : null;
            String path = matcher.group(3) != null
                    ? matcher.group(4)
                    : matcher.group(2);
            return new ResourceMetadata(matcher.group(1), objectType, path);
        }
        return null;
    }
}
