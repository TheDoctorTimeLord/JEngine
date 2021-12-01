package ru.test.annotation.battle.commands.damage;

import java.util.List;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.executioncontexts.NoneParameters;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.test.annotation.battle.events.TestHitEvent;

public class TestHit implements AdditionalBattleCommand<NoneParameters> {
    private final Point nextPosition;

    public TestHit(Point nextPosition) {
        this.nextPosition = nextPosition;
    }

    @Override
    public NoneParameters createParametersTemplate() {
        return NoneParameters.INSTANCE;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void perform(BattleModel model, BattleContext battleContext, NoneParameters executionParameters) {
        int modelId = model.getId();
        List<Integer> enemies = battleContext.getBattleState().getOnPosition(nextPosition);
        if (!enemies.isEmpty()) {
            DispatcherBattleWrapper dispatcher = battleContext.getDispatcher();
            enemies.forEach(enemy -> dispatcher.handle(new TestHitEvent(modelId, enemy, 1)));
        }
    }
}
