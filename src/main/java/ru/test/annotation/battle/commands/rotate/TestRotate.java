package ru.test.annotation.battle.commands.rotate;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.beancontainer.annotations.Bean;
import ru.test.annotation.battle.commands.rotate.RotateParameters.RotateOption;

@Bean
public class TestRotate implements BattleCommand<RotateParameters> {
    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public RotateParameters createParametersTemplate() {
        return new RotateParameters();
    }

    @Override
    public void perform(BattleModel model, BattleContext battleContext, RotateParameters parameters) {
        if (isExecutionParametersCorrect(parameters)) {
            CanMoved canMoved = (CanMoved)model;
            Direction oldDirection = canMoved.getDirection();
            canMoved.setDirection(parameters.getAnswer().equals(RotateOption.LEFT)
                    ? oldDirection.rotateLeft()
                    : oldDirection.rotateRight());
        }
    }

    private static boolean isExecutionParametersCorrect(RotateParameters parameters) {
        RotateOption answer = parameters.getAnswer();
        return parameters.getAvailableOptions().contains(answer);
    }
}
