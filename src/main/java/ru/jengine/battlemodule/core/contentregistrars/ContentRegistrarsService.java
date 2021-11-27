package ru.jengine.battlemodule.core.contentregistrars;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;

@BattleBeanPrototype
public class ContentRegistrarsService {
    private final List<ContentRegistrar> registrars;
    private PostHandlerBindingService postHandlerBindingService;

    public ContentRegistrarsService(List<ContentRegistrar> registrars) {
        this.registrars = registrars;
    }

    public void register(RegistrarsContext context) {
        postHandlerBindingService = context.getPostHandlerBindings();
        registrars.forEach(registrar -> registrar.register(context));
    }

    public void prepareToStopBattle() {
        postHandlerBindingService.removePostHandlers();
    }
}
