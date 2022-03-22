package ru.jengine.eventqueue.asyncdispatcher;

import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.eventpool.AsyncEventPoolHandler;

public class StubAsyncDispatcher implements AsyncDispatcher {
    @Override
    public void registerEventPoolHandler(AsyncEventPoolHandler eventPoolHandler) {}

    @Override
    public void removeEventPoolHandler(String eventPoolHandlerCode) {}

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) {}

    @Override
    public void registerPostHandlerToPool(String eventPoolCode, PostHandler<?> postHandler) {}

    @Override
    public void removePostHandlerFromPool(String eventPoolCode, PostHandler<?> postHandler) {}

    @Override
    public void removePostHandler(PostHandler<?> postHandler) {}

    @Override
    public void startDispatcher() {}

    @Override
    public void stopDispatcher() {}
}
