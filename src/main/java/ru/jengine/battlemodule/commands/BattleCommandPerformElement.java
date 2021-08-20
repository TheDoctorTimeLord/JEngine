package ru.jengine.battlemodule.commands;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.battlemodule.models.BattleModel;

public class BattleCommandPerformElement<P extends CommandExecutionParameters> {
    private final int battleModelId;
    private final BattleCommand<P> battleCommand;
    private final P executionParameters;

    public BattleCommandPerformElement(int battleModelId, BattleCommand<P> battleCommand, P executionParameters) {
        this.battleModelId = battleModelId;
        this.battleCommand = battleCommand;
        this.executionParameters = executionParameters;
    }

    public void performCommand(BattleContext commandContext) {
        BattleModel model = commandContext.getBattleObjectsManager().resolve(battleModelId);
        battleCommand.perform(model, commandContext, executionParameters);
    }

    public BattleCommand<P> getBattleCommand() {
        return battleCommand;
    }
}
