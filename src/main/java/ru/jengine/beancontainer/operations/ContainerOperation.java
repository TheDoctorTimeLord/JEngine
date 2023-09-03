package ru.jengine.beancontainer.operations;

import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.exceptions.ContainerException;

import java.util.function.BiFunction;

public abstract class ContainerOperation {
    public static final String CHECK_EXCEPTION_CODE = "[check]";

    public abstract void apply(OperationResult previouseOperationResult, ContainerState state);

    @SuppressWarnings("unchecked")
    protected <T, R extends T> R getResult(OperationResult operationResult, String resultKey, Class<T> expectedResultType) {
        return (R) checkResult(operationResult, resultKey, expectedResultType, OperationResult::getResult);
    }

    @SuppressWarnings("unchecked")
    protected <T, R extends T> R poolResult(OperationResult operationResult, String resultKey, Class<T> expectedResultType) {
        return (R) checkResult(operationResult, resultKey, expectedResultType, OperationResult::poolResult);
    }

    private Object checkResult(OperationResult operationResult, String resultKey, Class<?> expectedResultType,
            BiFunction<OperationResult, String, Object> extractResultOperation)
    {
        Object result = extractResultOperation.apply(operationResult, resultKey);
        if (!expectedResultType.isInstance(result)) {
            throw new ContainerException(
                    "Operation [%s] expected the prepared result with code [%s] and type [%s]"
                            .formatted(getClass(), resultKey, expectedResultType),
                    CHECK_EXCEPTION_CODE
            );
        }
        return result;
    }
}
