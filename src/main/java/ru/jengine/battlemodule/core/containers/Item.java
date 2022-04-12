package ru.jengine.battlemodule.core.containers;

/**
 * Интерфейс, описывающий тип предмета. Тип описывает основные данные про предмет, в том числе регулирует часть его
 * поведения
 */
public interface Item {
    /**
     * Получение кода предмета
     */
    String getItemCode();

    /**
     * Получение типа предмета
     */
    String getItemType();

    /**
     * Максимальное количество элементов в одном стеке
     */
    int getMaxItemCount();
}
