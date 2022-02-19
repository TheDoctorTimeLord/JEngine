package ru.jengine.battlemodule.core.contentregistrars;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.ExtendedBattleContext;
import ru.jengine.battlemodule.core.information.InformationCenter;

/**
 * Хранит расширенный контент боя, и {@link PostHandlerBindingService} для упрощённого добавления
 * {@link ru.jengine.eventqueue.event.PostHandler PostHandler}
 */
public class RegistrarsContext {
    private final ExtendedBattleContext extendedBattleContext;
    private final PostHandlerBindingService postHandlerBindings;

    public RegistrarsContext(ExtendedBattleContext extendedBattleContext,
            PostHandlerBindingService postHandlerBindings) {
        this.extendedBattleContext = extendedBattleContext;
        this.postHandlerBindings = postHandlerBindings;
    }

    public BattleContext getBattleContext() {
        return extendedBattleContext.getBattleContext();
    }

    public InformationCenter getInformationCenter() {
        return extendedBattleContext.getInformationCenter();
    }

    public PostHandlerBindingService getPostHandlerBindings() {
        return postHandlerBindings;
    }
}
