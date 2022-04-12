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

    /**
     * Добавляет задачу, которая будет после завершения инициализации боя
     * @param task запланированная задача
     */
    protected void registerTaskAfterInitializeBattle(Task task) {
        registrarsContext.getBattleContext().getTaskRegistrar().addTaskAfterInitializeBattle(task);
    }

    /**
     * Добавляет задачу, которая будет исполнена в момент перед началом нового хода
     * @param task запланированная задача
     */
    protected void registerTaskBeforeTurn(Task task) {
        registrarsContext.getBattleContext().getTaskRegistrar().addTaskBeforeTurn(task);
    }

    /**
     * Добавляет задачу, которая будет исполнена в момент после окончания фазы
     * @param task запланированная задача
     */
    protected void registerTaskAfterPhase(Task task) {
        registrarsContext.getBattleContext().getTaskRegistrar().addTaskAfterPhase(task);
    }

    /**
     * Добавляет задачу, которая будет исполнена в момент после окончания хода
     * @param task запланированная задача
     */
    protected void registerTaskAfterTurn(Task task) {
        registrarsContext.getBattleContext().getTaskRegistrar().addTaskAfterTurn(task);
    }

    /**
     * Добавляет переиспользуюмую задачу, которая будет исполнена в момент перед началом нового хода
     * @param task запланированная переиспользуемая задача
     */
    protected void registerReusableTaskBeforeTurn(ReusableTask task) {
        registerTaskBeforeTurn(task);
    }

    /**
     * Добавляет переиспользуюмую задачу, которая будет исполнена в момент после окончания фазы
     * @param task запланированная переиспользуемая задача
     */
    protected void registerReusableTaskAfterPhase(ReusableTask task) {
        registerTaskAfterPhase(task);
    }

    /**
     * Добавляет переиспользуюмую задачу, которая будет исполнена в момент после окончания хода
     * @param task запланированная переиспользуемая задача
     */
    protected void registerReusableTaskAfterTurn(ReusableTask task) {
        registerTaskAfterTurn(task);
    }

    /**
     * Добавляет {@link PostHandler} и регистрирует его в {@link ru.jengine.eventqueue.Dispatcher Dispatcher}
     * @param postHandler добавляемый пост обработчик
     * @param <E> тип событий, который обрабатывает регистрируемый {@link PostHandler}
     */
    protected <E extends BattleEvent> void registerPostHandler(PostHandler<E> postHandler) {
        registrarsContext.getPostHandlerBindings().addPostHandler(postHandler);
    }
}
