package ru.jengine.battlemodule.core.information;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.utils.ServiceLocator;

/**
 * С технической точки зрения, динамические объекты, находясь в бою, не знают ничего о происходящем вокруг них. Они
 * знают только собственное состояние и команды, которые они могут выполнить. Данный интерфейс описывает объект,
 * который управляет информацией об окружающем мире для каждого динамического объекта. Используя объект, реализующий
 * данный интерфейс, можно узнать что видит, слышит и чувствует персонаж на момент начала хода. Предоставляется
 * информация с помощью специальных {@link InformationService информационных сервисов}
 */
public interface InformationCenter extends ServiceLocator<InformationService> {
    /**
     * Инициализирует центр информации
     * @param battleContext контекст текущего боя
     */
    void initialize(BattleContext battleContext);
}
