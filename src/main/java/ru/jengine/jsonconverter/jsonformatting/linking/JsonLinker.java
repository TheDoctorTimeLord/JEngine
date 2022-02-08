package ru.jengine.jsonconverter.jsonformatting.linking;

import java.util.List;
import java.util.Map;

import ru.jengine.jsonconverter.exceptions.JsonConvertException;
import ru.jengine.jsonconverter.resources.ResourceLoader;

public interface JsonLinker<JO> {
    Map<String, String> getDependedJsons(JO json, List<String> fieldsDependencies, ResourceLoader resourceLoader)
            throws JsonConvertException;
}
