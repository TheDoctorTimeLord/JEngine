package ru.jengine.battlemodule.core.events;

import ru.jengine.eventqueue.event.PostHandler;

import com.google.common.base.Objects;

/**
 * Обёртка над {@link PostHandler}, позволяющая в {@link ru.jengine.eventqueue.Dispatcher Dispatcher'e} выполнять
 * только обработчики, относящиеся к текущему бою
 * //TODO Вероятно, что механизм обработки быстрых событий будет изменён после рефакторинга Dispatcher
 * @param <E>
 */
public class BattlePostHandlerWrapper<E extends BattleEvent> implements PostHandler<E> {
    private final String battleId;
    private final PostHandler<E> wrapped;

    public BattlePostHandlerWrapper(String battleId, PostHandler<E> wrapped) {
        this.battleId = battleId;
        this.wrapped = wrapped;
    }

    @Override
    public Class<E> getHandlingEventType() {
        return wrapped.getHandlingEventType();
    }

    @Override
    public void handle(E event) {
        //Проверяет, что пришедшее событие относится к бою, в котором был зарегистрирован handler
        if (battleId.equals(event.getBattleId())) {
            wrapped.handle(event);
        }
    }

    @Override
    public int getPriority() {
        return wrapped.getPriority();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BattlePostHandlerWrapper<?> that = (BattlePostHandlerWrapper<?>)o;
        return Objects.equal(battleId, that.battleId) && Objects.equal(
                wrapped, that.wrapped);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(battleId, wrapped);
    }
}
