package ru.jengine.battlemodule.core;

/**
 * Генератор уникальных ID для сущностей в бою. ID должны быть каждый раз уникальные
 */
public interface IdGenerator {
    /**
     * Генерирует уникальный ID
     */
    int generateId();
}
