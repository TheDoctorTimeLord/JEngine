package ru.jengine.beancontainer.operations;

import java.util.List;
import java.util.Map;
import ru.jengine.beancontainer.modules.Module;

public interface ResultConstants {
    /**
     * Хранит словарь отображений названий контекстов в список найденных для него модулей. Хранимое значение:
     * <pre>{@link Map}<{@link String}, {@link List}<{@link Module}>></pre>
     */
    String MODULES_BY_CONTEXT = "modulesByContext";
}
