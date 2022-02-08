package ru.jengine.jsonconverter.jsonformatting.formatters;

import java.util.List;
import java.util.Map;

public interface JsonFormattersManager<JO> {
    JO correct(JO jsonObject);
    List<String> extractJsonDependencies(JO jsonObject);

    /**
     * Форматирует JSON, применяя внедрение зависимостей или добавления и изменяя поля JSON
     * @param mainJson JSON, которые должен быть форматирован
     * @param dependencies зависимости, которые были подобраны для форматирования JSON
     * @return true, если для получившегося JSON необходимо ввести повторную обработку корректирования и внедрения
     * зависимостей, false - иначе
     */
    boolean formatting(JO mainJson, Map<String, JO> dependencies);
    void cleanResultObject(JO resultJson);
}
