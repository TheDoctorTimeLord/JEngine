package ru.jengine.jsonconverter.jsonformatting.formatters;

import java.util.List;
import java.util.Map;

public interface JsonFormatter<JO> {
    List<String> getHandledTypes();
    JO correct(JO jsonObject);
    List<String> extractJsonDependencies(JO jsonObject);
    boolean formatting(JO mainJson, Map<String, JO> dependencies);
    void cleanResultObject(JO resultObject);
}
