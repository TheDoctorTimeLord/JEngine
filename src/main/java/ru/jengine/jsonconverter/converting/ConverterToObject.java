package ru.jengine.jsonconverter.converting;

import ru.jengine.jsonconverter.JsonContext;

public interface ConverterToObject {
    <T> T convertToObject(JsonContext jsonContext, Class<T> convertedResultClass);
}
