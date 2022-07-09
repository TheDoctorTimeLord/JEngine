package ru.jengine.eventqueue.event;

import ru.jengine.utils.HasPriority;

public interface PostHandler <E extends Event> extends EventHandler<E>, HasPriority { }
