package ru.jengine.battlemodule.core;

import ru.jengine.battlemodule.core.state.BattleState;

public interface BattleGenerator {
    void setIdGenerator(IdGenerator idGenerator);
    BattleState generate();
}
