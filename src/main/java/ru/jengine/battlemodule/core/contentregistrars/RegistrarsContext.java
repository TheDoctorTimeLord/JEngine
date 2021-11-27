package ru.jengine.battlemodule.core.contentregistrars;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.information.InformationCenter;

public class RegistrarsContext {
    private final BattleContext battleContext;
    private final InformationCenter informationCenter;
    private final PostHandlerBindingService postHandlerBindings;

    public RegistrarsContext(BattleContext battleContext, InformationCenter informationCenter,
            PostHandlerBindingService postHandlerBindings) {
        this.battleContext = battleContext;
        this.informationCenter = informationCenter;
        this.postHandlerBindings = postHandlerBindings;
    }

    public BattleContext getBattleContext() {
        return battleContext;
    }

    public InformationCenter getInformationCenter() {
        return informationCenter;
    }

    public PostHandlerBindingService getPostHandlerBindings() {
        return postHandlerBindings;
    }
}
