package ru.jengine.battlemodule.core.behaviors;

import java.util.List;
import java.util.Set;

import ru.jengine.battlemodule.core.commands.AdditionalBattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.BattleCommandPerformElement;
import ru.jengine.battlemodule.core.information.InformationCenter;
import ru.jengine.battlemodule.core.models.BattleModel;

/**
 * Определяет действия динамических объектов в игре. Поведения включают в себя: ИИ для мобов, управление персонажем
 * игрока, управление персонажами по сети, действия автоматизированных объектов и т.д. Работа поведения может быть
 * асинхронным (зависит от реализации {@link BehaviorObjectsManager}). Одно поведение может контролировать несколько
 * динамических сущностей
 */
public interface Behavior {
    /**
     * Определяет какими динамическими объектами в игре управляет текущее поведение.
     * @param dynamicObjects список доступных для выбора динамических моделей
     * @param informationCenter объект, предоставляющий данные боя, доступные персонажу
     * @return список ID объектов, которыми будет управлять поведение
     */
    Set<Integer> bind(List<BattleModel> dynamicObjects, InformationCenter informationCenter);

    /**
     * Отменяет управление переданным динамическим объект
     * @param unboundedId ID динамического объекта
     */
    void unbind(int unboundedId);

    /**
     * Выбирает действие, которое будет выполнять указанный динамический объект
     * @param characterId ID динамического объекта, выполняющего действие
     * @param availableCommands доступные для выбора команды
     * @return действие, которое будет исполнено указанным динамическим объектом
     */
    BattleCommandPerformElement<?> sendAction(int characterId, List<BattleCommand<?>> availableCommands);

    /**
     * Уточняет параметры для дополнительного действия, выполняемого динамическим объектом
     * @param characterId ID динамического объекта, выполняющего дополнительное действие
     * @param command дополнительное действие, для которого требуется уточнить параметры
     * @return действие, которое будет исполнено указанным динамическим объектом
     */
    BattleCommandPerformElement<?> handleAdditionalCommand(int characterId, AdditionalBattleCommand<?> command);
}
