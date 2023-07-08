package ru.jengine.eventqueue.event;

import ru.jengine.utils.serviceclasses.HasPriority;

public interface PostHandler <E extends Event> extends EventHandler<E>, HasPriority { }
