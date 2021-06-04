package ru.jengine.eventqueue.quantum;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;

public class QuantumEventInterceptor implements EventInterceptor, QuantumEventPoolRegistrar {
    private final List<QuantaEventPool> quantaEventPools = new CopyOnWriteArrayList<>();

    @Override
    public boolean isValid(Event event) {
        return event instanceof QuantumEvent;
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        if (event instanceof QuantumEvent) {
            //Три отдельных for нужны, чтобы событие квантования пришло во все пулы "одновременно"
            for (QuantaEventPool eventPool : quantaEventPools) {
                eventPool.lockEventPool();
            }

            for (QuantaEventPool eventPool : quantaEventPools) {
                eventPool.registerQuantumEvent((QuantumEvent) event);
            }

            for (QuantaEventPool eventPool : quantaEventPools) {
                eventPool.unlockEventPool();
            }
        }
    }

    @Override
    public void registerQuantumEventPool(QuantaEventPool eventPool) {
        quantaEventPools.add(eventPool);
    }
}
