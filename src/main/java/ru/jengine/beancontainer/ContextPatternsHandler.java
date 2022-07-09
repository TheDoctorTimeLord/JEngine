package ru.jengine.beancontainer;

import java.util.List;

import ru.jengine.beancontainer.exceptions.ContainerException;

public interface ContextPatternsHandler {
    void registerPattern(String patternName, ContextPattern contextPattern) throws ContainerException;
    void loadContext(String patternName);
    void loadContexts(List<String> patternNames);
    void loadCopiedContext(String copiedPatternName, String loadedPatternName);

    default void loadContext(String patternName, ContextPattern contextPattern) throws ContainerException {
        registerPattern(patternName, contextPattern);
        loadContext(patternName);
    }
}
