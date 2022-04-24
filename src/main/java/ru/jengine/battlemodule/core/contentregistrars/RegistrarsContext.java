package ru.jengine.battlemodule.core.contentregistrars;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.information.InformationCenter;

/**
 * Хранит расширенный контент боя, и {@link PostHandlerBindingService} для упрощённого добавления
 * {@link ru.jengine.eventqueue.event.PostHandler PostHandler}
 */
public class RegistrarsContext {
    private final BattleContext battleContext;
    private final PostHandlerBindingService postHandlerBindings;

    public RegistrarsContext(BattleContext battleContext, PostHandlerBindingService postHandlerBindings) {
        this.battleContext = battleContext;
        this.postHandlerBindings = postHandlerBindings;
    }

    public BattleContext getBattleContext() {
        return battleContext;
    }

    public InformationCenter getInformationCenter() {
        return battleContext.getInformationCenter();
    }

    public PostHandlerBindingService getPostHandlerBindings() {
        return postHandlerBindings;
    }
}
