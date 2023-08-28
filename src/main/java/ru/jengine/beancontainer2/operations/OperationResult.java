package ru.jengine.beancontainer2.operations;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class OperationResult {
    private final Map<String, Object> savedResults = new HashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T poolResult(String resultKey) {
        return (T) savedResults.remove(resultKey);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getResult(String resultKey) {
        return (T) savedResults.get(resultKey);
    }

    public void putResult(String resultKey, Object result) {
        savedResults.put(resultKey, result);
    }
}
