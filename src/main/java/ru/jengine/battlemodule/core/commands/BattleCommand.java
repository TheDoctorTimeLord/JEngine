package ru.jengine.battlemodule.core.commands;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.utils.serviceclasses.HasPriority;

/**
 * Описывает команды, которые будут исполнять динамический объект. Команда описывает одно конкретное (возможно сложное)
 * действие над объектом в бою. Команды могут быть двух типов: простые (выполняются в одну фазу) и
 * комплексные/сложные (выполняются в одну и более фаз). В процессе работы сложная команда может зарегистрировать
 * дополнительную команду, которая будет выполнена в следующий ход, дополнительная команда может зарегистрировать
 * следующую и т.д.<br>
 * ВАЖНО: Подразумевается, что все команды всех персонажей В РАМКАХ ОДНОЙ ФАЗЫ выполняются единомоменто. На самом
 * деле это иллюзия для пользователя. При выполнении конкретная команда, имея конкретные параметры, должна проверять
 * их на непротиворечивость.<br>
 * <i>Например, если вы делаете команду по перемещению объекта из одной клетки в другую,
 * и хотите, чтобы на одной клетке мог быть только один объект, вы ОБЯЗАНЫ при исполнении команды проверить, что на
 * выбранную клетку не был до вас перемещён персонаж. Иначе может оказаться, что другая команда до вашего исполнения
 * переместила в эту клетку объект при исполнении в ЭТУ ЖЕ ФАЗУ, делая параметры вашей команды невалидными</i>
 * @param <P> тип параметров, необходимых для этой команды
 */
public interface BattleCommand<P extends CommandExecutionParameters> extends HasPriority {
    /**
     * Создаёт шаблон параметров команды, который нужно заполнить поведению, исполняющему эту команду
     */
    P createParametersTemplate();

    /**
     * Выполняет команду для переданного динамического объекта, используя при этом переданные параметры команды
     * @param model динамический объект, над которым выполняется команда
     * @param battleContext контекст текущего боя
     * @param executionParameters параметры, уточняющие поведение команды (ТРЕБУЮТ ВАЛИДАЦИИ ПЕРЕД ИСПОЛНЕНИЕМ)
     */
    void perform(BattleModel model, BattleContext battleContext, P executionParameters);
}
