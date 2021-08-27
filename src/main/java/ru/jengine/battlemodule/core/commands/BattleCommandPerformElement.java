package ru.jengine.battlemodule.core.commands;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.models.BattleModel;

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
        BattleModel model = commandContext.getBattleState().resolveId(battleModelId);
        if (model != null) { //TODO костыль, убрать
            battleCommand.perform(model, commandContext, executionParameters);
        }
    }

    public BattleCommand<P> getBattleCommand() {
        return battleCommand;
    }

    @Override
    public String toString() {
        return "BattleCommandPerformElement{" +
                "battleModelId=" + battleModelId +
                ", battleCommand=" + battleCommand +
                ", executionParameters=" + executionParameters +
                '}';
    }
}
