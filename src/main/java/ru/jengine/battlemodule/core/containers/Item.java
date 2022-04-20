package ru.jengine.battlemodule.core.containers;

import ru.jengine.battlemodule.core.models.ObjectType;

/**
 * Интерфейс, описывающий тип предмета. Тип описывает основные данные про предмет, в том числе регулирует часть его
 * поведения
 */
public interface Item extends ObjectType {
    /**
     * Получение типа предмета
     */
    String getItemType();

    /**
     * Максимальное количество элементов в одном стеке
     */
    int getMaxItemCount();
}
