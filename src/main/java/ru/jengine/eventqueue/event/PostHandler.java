package ru.jengine.eventqueue.event;

import ru.jengine.beancontainer.service.HasPriority;

public interface PostHandler <E extends Event> extends EventHandler<E>, HasPriority { }
