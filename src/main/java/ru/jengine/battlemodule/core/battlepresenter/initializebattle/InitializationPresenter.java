package ru.jengine.battlemodule.core.battlepresenter.initializebattle;

import java.util.List;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.battlepresenter.BattleAction;

/**
 * Описывает информацию о начальном состоянии боя. Для более подробной информации, зачем это нужно смотри:
 * {@link ru.jengine.battlemodule.core.battlepresenter.BattleActionPresenter} и
 * {@link ru.jengine.battlemodule.core.BattleMaster}
 */
public interface InitializationPresenter {
    /**
     * Предоставляет события, которые были собраны за период инициализации боя
     * @param battleContext контекст боя, хранящий все данные о бое
     * @return список действий в бою, либо пустой список, если никаких действий не произошло
     */
    List<BattleAction> presentBattleInitialize(BattleContext battleContext);
}
