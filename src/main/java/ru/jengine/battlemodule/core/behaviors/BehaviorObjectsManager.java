package ru.jengine.battlemodule.core.behaviors;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.core.information.InformationCenter;
import ru.jengine.battlemodule.core.models.BattleModel;

import com.google.common.collect.Multimap;

/**
 * Менеджер всех {@link Behavior поведений} в бою. Через него осуществляются вся работа с поведениями динамических
 * объектов в бою
 */
public interface BehaviorObjectsManager {
    /**
     * Связывает динамические объекты в бою с их поведением. Также происходит дополнительная инициализация поведений
     * менеджером информации в бою.
     * @param allDynamicObjects все динамические объекты в бою
     * @param informationCenter объект, предоставляющий данные боя, доступные персонажу
     */
    void bindBehaviors(List<BattleModel> allDynamicObjects, InformationCenter informationCenter);

    /**
     * Отвязывает поведение от динамического объекта
     * @param dynamicObjectId ID объекта в бою
     */
    void unbindBehavior(int dynamicObjectId);

    /**
     * Получает команду, которую будет выполнять персонаж в этом ходу
     * @param availableCommands сопоставление динамического объекта и списка команд, доступных ему для выполнения
     * @return исполняемая объектом команда
     */
    Collection<BattleCommandPerformElement<?>> extractNewCommands(Map<Integer, List<BattleCommand<?>>> availableCommands);

    /**
     * Получает дополнительную информацию о дополнительном действии текущей команды
     * @param additionalCommands  сопоставление динамического объекта и дополнительных команд, которые он выполняет
     * @return исполняемая объектом дополнительная команда
     */
    Collection<BattleCommandPerformElement<?>> handleAdditionalCommands(Multimap<Integer, AdditionalBattleCommand<?>> additionalCommands);
}
