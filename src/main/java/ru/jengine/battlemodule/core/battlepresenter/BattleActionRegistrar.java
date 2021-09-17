package ru.jengine.battlemodule.core.battlepresenter;

public interface BattleActionRegistrar {
    void registerAction(BattleAction action);
    void declareNewPhase();
    void declareNewTurn();
}
