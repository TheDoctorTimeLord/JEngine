package ru.jengine.battlemodule.core.contentregistrars;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.information.InformationService;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.taskscheduler.ReusableTask;
import ru.jengine.taskscheduler.Task;

/**
 * Абстрактный класс для {@link ContentRegistrar}, который предоставляет удобные методы для регистрации разного
 * контента боя.
 */
public abstract class AbstractContentRegistrar implements ContentRegistrar {
    private RegistrarsContext registrarsContext;
    protected BattleContext battleContext;

    @Override
    public void register(RegistrarsContext context) {
        this.registrarsContext = context;
        this.battleContext = context.getBattleContext();
        registerInt();
    }

    /**
     * Регистрирует часть контента боя
     */
    protected abstract void registerInt();

    protected <T extends InformationService, S extends T> void registerInformationService(Class<T> serviceType, S service) {
        registrarsContext.getInformationCenter().setService(serviceType, service);
    }

    protected void registerTaskBeforeTurn(Task task) {
        registrarsContext.getBattleContext().getTaskRegistrar().addTaskBeforeTurn(task);
    }

    protected void registerTaskAfterPhase(Task task) {
        registrarsContext.getBattleContext().getTaskRegistrar().addTaskAfterPhase(task);
    }

    protected void registerTaskAfterTurn(Task task) {
        registrarsContext.getBattleContext().getTaskRegistrar().addTaskAfterTurn(task);
    }

    protected void registerReusableTaskBeforeTurn(ReusableTask task) {
        registerTaskBeforeTurn(task);
    }

    protected void registerReusableTaskAfterPhase(ReusableTask task) {
        registerTaskAfterPhase(task);
    }

    protected void registerReusableTaskAfterTurn(ReusableTask task) {
        registerTaskAfterTurn(task);
    }

    protected <E extends BattleEvent> void registerPostHandler(PostHandler<E> postHandler) {
        registrarsContext.getPostHandlerBindings().addPostHandler(postHandler);
    }
}
