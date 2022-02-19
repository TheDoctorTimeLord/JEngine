package ru.jengine.battlemodule.core;

import ru.jengine.battlemodule.core.state.BattleState;

/**
 * Генератор состояния боя. Создаёт или восстанавливает все данные, касающиеся объектов в бою и карты местности.
 */
public interface BattleGenerator {
    /**
     * Устанавливает генератор ID для работы с объектами в бою
     * @param idGenerator генератор ID
     */
    void setIdGenerator(IdGenerator idGenerator);

    /**
     * Генерирует начальное состояние боя.
     * @return сгенерированное состояние боя
     */
    BattleState generate();
}
