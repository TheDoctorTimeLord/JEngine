package ru.test.annotation.battle;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.behaviors.Behavior;
import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.core.commands.executioncontexts.NoneParameters;
import ru.jengine.battlemodule.core.exceptions.BattleException;
import ru.jengine.battlemodule.core.information.InformationCenter;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.utils.RandomUtils;
import ru.test.annotation.battle.commands.damage.TestHit;
import ru.test.annotation.battle.commands.damage.TestMoveAndHit;
import ru.test.annotation.battle.commands.move.MoveParameters;
import ru.test.annotation.battle.commands.move.TestCommandMove;
import ru.test.annotation.battle.commands.rotate.RotateParameters;
import ru.test.annotation.battle.commands.rotate.RotateParameters.RotateOption;
import ru.test.annotation.battle.commands.rotate.TestRotate;
import ru.test.annotation.battle.information.NextCellScannerService;

@BattleBeanPrototype
public class SimpleBehavior implements Behavior {
    private final Random random = new Random();
    private NextCellScannerService nextCellScannerService;

    @Override
    public Set<Integer> bind(List<BattleModel> dynamicObjects, InformationCenter informationCenter) {
        this.nextCellScannerService = informationCenter.getService(NextCellScannerService.class);
        return dynamicObjects.stream()
                .map(BattleModel::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public void unbind(int unboundedId) { }

    @Override
    public BattleCommandPerformElement<?> sendAction(int characterId, List<BattleCommand<?>> availableCommands) {
        TestRotate rotate = null;
        TestCommandMove commandMove = null;
        TestHit hit = null;
        TestMoveAndHit moveAndHit = null;

        for (BattleCommand<?> command : availableCommands) {
            if (command instanceof TestRotate) {
                rotate = (TestRotate)command;
            } else if (command instanceof TestMoveAndHit) {
                moveAndHit = (TestMoveAndHit)command;
            } else if (command instanceof TestHit) {
                hit = (TestHit)command;
            } else {
                commandMove = (TestCommandMove)command;
            }
        }

        if (rotate == null) {
            throw new BattleException("Something wrong with commands! Handle: " + characterId);
        }

        if (moveAndHit != null) {
            return sendActionWithNoneParams(characterId, moveAndHit);
        }

        if (hit != null) {
            return sendActionWithNoneParams(characterId, hit);
        }

        commandMove = RandomUtils.testInPercent(90) ? commandMove : null;

        if (commandMove != null) {
            MoveParameters params = prepareParameters(characterId, commandMove);
            return new BattleCommandPerformElement<>(characterId, commandMove, params);
        }

        RotateOption answer = RandomUtils.testInPercent(50) ? RotateOption.LEFT : RotateOption.RIGHT;
        RotateParameters params = rotate.createParametersTemplate().setAnswer(answer);
        return new BattleCommandPerformElement<>(characterId, rotate, params);
    }

    @Override
    public BattleCommandPerformElement<?> handleAdditionalCommand(int characterId, AdditionalBattleCommand<?> command) {
        return null;
    }

    private static BattleCommandPerformElement<?> sendActionWithNoneParams(int characterId,
            BattleCommand<NoneParameters> command)
    {
        NoneParameters noneParameters = command.createParametersTemplate();
        return new BattleCommandPerformElement<>(characterId, command, noneParameters);
    }

    private MoveParameters prepareParameters(int characterId, TestCommandMove commandMove) {
        MoveParameters params = commandMove.createParametersTemplate();
        Set<Point> availableMoveCells = params.getAvailablePoints();
        Point nextCell = nextCellScannerService.getNextCell(characterId);

        if (availableMoveCells.contains(nextCell) && nextCellScannerService.hasEnemyInNextTwoCells(characterId)) {
            params.publishAnswer(nextCell);
        } else {
            params.publishAnswer(RandomUtils.chooseInCollection(params.getAvailablePoints()));
        }

        return params;
    }
}
