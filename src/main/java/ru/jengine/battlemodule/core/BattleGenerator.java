package ru.jengine.battlemodule.core;

import ru.jengine.battlemodule.core.state.BattleState;

/**
 * Генератор состояния боя. Создаёт или восстанавливает все данные, касающиеся объектов в бою и карты местности.
 */
public abstract class BattleGenerator {
    protected IdGenerator idGenerator;

    /**
     * Устанавливает генератор ID для работы с объектами в бою
     * @param idGenerator генератор ID
     */
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Генерирует начальное состояние боя.
     * @return сгенерированное состояние боя
     */
    public abstract BattleState generate();
}
