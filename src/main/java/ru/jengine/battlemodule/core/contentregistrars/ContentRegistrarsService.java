package ru.jengine.battlemodule.core.contentregistrars;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;

/**
 * Сервис, регистрирующий весь контент, используемый в бою. Под контентом понимаются:
 * {@link ru.jengine.battlemodule.core.commands.BattleCommand команды},
 * {@link ru.jengine.battlemodule.core.information.InformationService сервисы информации} и т.д.
 */
@BattleBeanPrototype
public class ContentRegistrarsService {
    private final List<ContentRegistrar> registrars;
    private PostHandlerBindingService postHandlerBindingService;

    public ContentRegistrarsService(List<ContentRegistrar> registrars) {
        this.registrars = registrars;
    }

    /**
     * Регистрирует весь контент, доступный в бою
     * @param context контекст, необходимый для регистрации контента
     */
    public void register(RegistrarsContext context) {
        postHandlerBindingService = context.getPostHandlerBindings();
        registrars.forEach(registrar -> registrar.register(context));
    }

    /**
     * Подготавливает бой к завершению. Освобождает часть ресурсы, занятые при старте боя
     */
    public void prepareToStopBattle() {
        postHandlerBindingService.removePostHandlers();
    }
}
