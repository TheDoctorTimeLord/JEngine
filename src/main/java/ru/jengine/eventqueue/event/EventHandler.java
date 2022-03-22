package ru.jengine.eventqueue.event;

public interface EventHandler <E extends Event> { //TODO научится обрабатывать событие с подсобытиями
    Class<E> getHandlingEventType(); //TODO научится исправлять через рефлексию (см. видео Борисова)
    void handle(E event);
}
