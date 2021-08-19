package ru.jengine.battlemodule;

import ru.jengine.battlemodule.state.BattleState;

public interface BattleGenerator {
    void setIdGenerator(IdGenerator idGenerator);
    BattleState generate();
}
