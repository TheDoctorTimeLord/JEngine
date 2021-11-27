package ru.jengine.battlemodule.core.contentregistrars;

import java.util.ArrayList;
import java.util.List;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.eventqueue.event.PostHandler;

public class PostHandlerBindingService {
    private final List<Binding<?>> bindings = new ArrayList<>();
    private final DispatcherBattleWrapper dispatcher;

    public PostHandlerBindingService(DispatcherBattleWrapper dispatcher) {
        this.dispatcher = dispatcher;
    }

    public <E extends BattleEvent> void addPostHandler(PostHandler<E> postHandler) {
        bindings.add(new Binding<>(postHandler));
        dispatcher.registerPostHandler(postHandler);
    }

    public void removePostHandlers() {
        bindings.forEach(binding -> dispatcher.removePostHandler(binding.getHandler()));
    }

    private static class Binding<E extends BattleEvent> {
        private final PostHandler<E> handler;

        private Binding(PostHandler<E> handler) {
            this.handler = handler;
        }

        public PostHandler<E> getHandler() {
            return handler;
        }
    }
}
