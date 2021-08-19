package ru.jengine.battlemodule.commands;

import ru.jengine.battlemodule.BattleContext;

public class BattleCommandPerformElement {
    private final int battleModelId;
    private final BattleCommand battleCommand;

    public BattleCommandPerformElement(int battleModelId, BattleCommand battleCommand) {
        this.battleModelId = battleModelId;
        this.battleCommand = battleCommand;
    }

    public void performCommand(BattleContext commandContext) {
        battleCommand.perform(commandContext.getBattleObjectsManager().resolve(battleModelId), commandContext);
    }

    public BattleCommand getBattleCommand() {
        return battleCommand;
    }
}
